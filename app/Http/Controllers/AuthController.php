<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Arr;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use App\Models\User;
use App\Models\Supir;
use Carbon\Carbon;
use App\Models\Kendaraan;

class AuthController extends BaseController
{

    public function register(Request $request)
    {
        $validator = \Validator::make($request->all(), [
            'username' => 'required',
            'no_telepon' => 'required',
            'password' => 'required',
            'c_password' => 'required',
            'role' => 'required',
            'nama'=> 'required'
        ]);
   
        if($validator->fails()){
            return $this->sendError('Validation Error.', $validator->errors());       
        }

        if($request->password!=$request->c_password){
            $response = [
                'success' => true,
                'status' => 'error',
                'errors' => null,
                'content' => null,
                'massage' => "Password tidak sama",
            ];
    
            return response()->json($response, 200);
        }else{
            $user = User::all();

            foreach($user as $p){
                if($p->username == $request->username){
                    $respon = [
                        'success' => true,
                        'status' => 'error',
                        'massage' => 'terdaftar',
                        'errors' => null,
                        'content' => null,
                    ];
                    return response()->json($respon, 200);
                }
            }
       
            $input = $request->all();
            $input['password'] = bcrypt($input['password']);
            $user2 = User::create($input);
            $success['token'] =  $user2->createToken('MyApp')->plainTextToken;
            $success['name'] =  $user2->nama;
            
       
            // return $this->sendResponse($success, 'User register successfully.');

            if($request->role=="supir"){
                $kendaraan = [
                    'id_supir' => $user2->id,
                    'jenis_kendaraan' => null,
                    'muatan_kendaraan' => null,
                    'stat' => "not update"
                ];

                 DB::table('kendaraan')->insert($kendaraan);
            }
    
            $response = [
                'success' => true,
                'status'  => "ok",
               ' errors' => null,
                'content' => null,
                'massage' => "Sukses mendaftar.",
            ];
    
            return response()->json($response, 200);
        }

            
    }


    function login(Request $request){

        $validate = \Validator::make($request->all(), [
            'username' => 'required',
            'password' => 'required',
            'role' => 'required'
        ]);

        if ($validate->fails()) {
            $respon = [
                'status' => 'error',
                'msg' => 'Validator error',
                'errors' => $validate->errors(),
                'content' => null,
            ];
            return response()->json($respon, 200);
        } else {
            
            $user = null;
            if($request->role =='supir'){
                $user = DB::table('user')
                ->join('kendaraan', 'kendaraan.id_supir', '=', 'user.id')
                ->where('user.username', $request->username)
                ->where('user.role', $request->role)
                ->first();
            }else{
                $user = User::where('username', $request->username)
                ->where('role', $request->role)
                ->first();
            }

            if($user!=null){
                if (! \Hash::check($request->password, $user->password, [])) {
                    $respon = [
                        'status' => 'false',
                        'massage' => 'Wronng Password',
                        'errors' => null,
                        'data' => null,
                        'status_code' => 200,
                    ];
                    return response()->json($respon, 200);
                }
    
                $respon = [
                    'status' => 'success',
                    'massage' => 'Login successfully',
                    'errors' => null,
                    'data' => $user,
                    'status_code' => 200,
                ];
                return response()->json($respon, 200);
            }else{
                $respon = [
                    'status' => 'false',
                    'massage' => 'Akunt tidak ditemukan',
                    'errors' => null,
                    'data' => null,
                    'status_code' => 200,
                ];
                return response()->json($respon, 200);
            }
         
        }
    }

    public function logout(Request $request) {
        $user = $request->user();
        $user->currentAccessToken()->delete();
        $respon = [
            'status' => 'success',
            'msg' => 'Logout successfully',
            'errors' => null,
            'content' => null,
        ];
        return response()->json($respon, 200);
    }

    public function logoutall(Request $request) {
        $user = $request->user();
        $user->tokens()->delete();
        $respon = [
            'status' => 'success',
            'msg' => 'Logout successfully',
            'errors' => null,
            'content' => null,
        ];
        return response()->json($respon, 200);
    }


    public function validatePasswordRequest(Request $request){
        $user = DB::table('users')->where('email', '=',  $request->email)->first();
            // Check if the user exists
            if ($user == null) {
                $respon = [
                    'status' => 'success',
                    'msg'=> 'Not Found',
                ];
            }else{

                //Create Password Reset Token
                DB::table('password_resets')->insert([
                    'email' => $request->email,
                    'token' => Str::random(60),
                    'created_at' => Carbon::now()
                ]);
                //Get the token just created above
                $tokenData = DB::table('password_resets')
                    ->where('email',  $request->email)->first();
                
                if ($this->sendResetEmail( $request->email, $tokenData->token)) {

                } else {
                    $respon = [
                        'status' => 'error',
                        'msg' => 'Unathorized',
                        'errors' => null,
                        'content' => null,
                    ];
                    return response()->json($respon, 401);
                
                    // return redirect()->back()->withErrors(['error' => trans('A Network Error occurred. Please try again.')]);
                }
            }
    }


    private function sendResetEmail($email, $token)
        {
        //Retrieve the user from the database
        $user = DB::table('users')->where('email', $email)->select('name', 'email')->first();
        //Generate, the password reset link. The token generated is embedded in the link
        $link = config('base_url') . 'password/reset/' . $token . '?email=' . urlencode($user->email);

        try {
            //Here send the link with CURL with an external email API 
            $respon = [
                'status' => 'success',
                'msg' => 'Login successfully',
                'errors' => null,
                'content' => [
                    'status_code' => 200,
                    'access_token' => $token,
                    'token_type' => 'Bearer',
                ]
            ];
            
                return true;
            } catch (\Exception $e) {
                return false;
            }
        }

        public function resetPassword(Request $request)
            {
                //Validate input
                $validator = Validator::make($request->all(), [
                    'email' => 'required|email|exists:users,email',
                    'password' => 'required|confirmed',
                    'token' => 'required' ]);

                //check if payload is valid before moving on
                if ($validator->fails()) {
                    return redirect()->back()->withErrors(['email' => 'Please complete the form']);
                }

                $password = $request->password;
            // Validate the token
                $tokenData = DB::table('password_resets')
                ->where('token', $request->token)->first();
            // Redirect the user back to the password reset request form if the token is invalid
                if (!$tokenData) return view('auth.passwords.email');

                $user = User::where('email', $tokenData->email)->first();
            // Redirect the user back if the email is invalid
                if (!$user) return redirect()->back()->withErrors(['email' => 'Email not found']);
            //Hash and update the new password
                $user->password = \Hash::make($password);
                $user->update(); //or $user->save();

                //login the user immediately they change password successfully
                Auth::login($user);

                //Delete the token
                DB::table('password_resets')->where('email', $user->email)
                ->delete();

                //Send Email Reset Success Email
                if ($this->sendSuccessEmail($tokenData->email)) {
                    return view('index');
                } else {
                    return redirect()->back()->withErrors(['email' => trans('A Network Error occurred. Please try again.')]);
                }

            }

            public function updateProfil(Request $request){
                $user = User::find($request->id);

                if($request->nama !=null){
                    $user->nama = $request->nama;
                }

               if( $request->no_rek!=null){
                    $user->no_tf = $request->no_rek;
                }

                if($request->lat !=null){
                    $user->lat = $request->lat;
                }

                if($request->lon !=null){
                    $user->lon = $request->lon;
                }
                
                if($request->no_telepon !=null||$request->no_telepon!="null"){
                    $user->no_telepon = $request->no_telepon;
                }

                if($request->alamat !=null){
                    $user->alamat = $request->alamat;
                }
                if($request->file('img')!=null){
                    $fileName = time().$request->file('img')->getClientOriginalName();
                    $request->file('img')->move(public_path('uploads'), $fileName);
                    $user->poto_profil = $fileName;

                }
                
              
               
                $kendaraan = Kendaraan::where('id_supir', $user->id)->first();


                
                if($kendaraan!=null){
                    if($kendaraan->jenis_kendaraan!="null" && $kendaraan->muatan_kendaraan<=$kendaraan->muatan_terpenuhi){
                        $kendaraan->stat = "Tersedia";
                    }
    
                    if($request->jenis_kendaraan !=null){
                        $kendaraan->jenis_kendaraan = $request->jenis_kendaraan;
                    }
                    
                    if($request->muatan_kendaraan !=null){
                        $kendaraan->muatan_kendaraan = $request->muatan_kendaraan;
                    }
                    
                    if($request->lokasi_awal !=null){
                        $kendaraan->lokasi_awal = $request->lokasi_awal;
                   }
                    
                    if($request->lokasi_akhir !=null){
                        $kendaraan->lokasi_akhir = $request->lokasi_akhir;
                    }
                    
                    if($user->update() && $kendaraan->update()){
                        
                $user2 = null;
                if($user->role =='supir'){
                    $user2 = DB::table('user')
                    ->join('kendaraan', 'kendaraan.id_supir', '=', 'user.id')
                    ->where('user.username', $user->username)
                    ->where('user.role', $user->role)
                    ->first();
                }else{
                    $user2 = User::where('username', $user->username)
                    ->where('role', $user->role)
                    ->first();
                }

                        $response = [
                            'status' => 'ok',
                            'massage' => 'Sukses mengupdate profil',
                            'errors' => null,
                            'content' => null,
                            'user' => $user2,
                        ];
                        return response()->json($response, 200);
                    }
                }else{
                    if($user->update()){
                        
                $user2 = null;
                if($user->role =='supir'){
                    $user2 = DB::table('user')
                    ->join('kendaraan', 'kendaraan.id_supir', '=', 'user.id')
                    ->where('user.username', $user->username)
                    ->where('user.role', $user->role)
                    ->first();
                }else{
                    $user2 = User::where('username', $user->username)
                    ->where('role', $user->role)
                    ->first();
                }

                        $response = [
                            'status' => 'ok',
                            'massage' => 'Sukses mengupdate profil',
                            'errors' => null,
                            'content' => null,
                            'user' => $user2,
                        ];
                        return response()->json($response, 200);
                    }
                }
              

            }
}
