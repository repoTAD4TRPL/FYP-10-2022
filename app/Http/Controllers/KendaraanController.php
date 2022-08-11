<?php

namespace App\Http\Controllers;

use App\Models\Kendaraan;
use Illuminate\Http\Request;
use DB;

class KendaraanController extends BaseController
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
  public function index($lat, $long)
    {
        $kendaraan = DB::table('kendaraan')
        ->join('user', 'kendaraan.id_supir','=','user.id')
        ->where('kendaraan.lokasi_awal','!=','')
        ->where('kendaraan.lokasi_akhir','!=','')
        ->get();
        
        foreach($kendaraan as $k){
            $k->jarak =  $this->haversineGreatCircleDistance( $lat, $long, $k->lat, $k->lon)*0.001;
        }

            $response = [
                'status' => 'success',
                'massage' => 'Semua data kendaraan',
                'errors' => null,
                'content' => null,
                'data' => $kendaraan,
            ];
            return response()->json($response, 200);
            
    }
    
     function haversineGreatCircleDistance(
        $latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371000)
      {
        $latFrom = deg2rad($latitudeFrom);
        $lonFrom = deg2rad($longitudeFrom);
        $latTo = deg2rad($latitudeTo);
        $lonTo = deg2rad($longitudeTo);
      
        $lonDelta = $lonTo - $lonFrom;
        $a = pow(cos($latTo) * sin($lonDelta), 2) +
          pow(cos($latFrom) * sin($latTo) - sin($latFrom) * cos($latTo) * cos($lonDelta), 2);
        $b = sin($latFrom) * sin($latTo) + cos($latFrom) * cos($latTo) * cos($lonDelta);
      
        $angle = atan2(sqrt($a), $b);
        return $angle * $earthRadius;
      }


    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $validator = \Validator::make($request->all(), [
            'id_supir' => 'required',
            'jenis_kendaraan' => 'required',
            'muatan_kendaraan' => 'required'
        ]);
   
        if($validator->fails()){
            return $this->sendError('Validation Error.', $validator->errors());       
        }

        $input = $request->all();
        $kendaraan = Kendaraan::create($input);

        if($kendaraan){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses menambahkan hasil tani',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Kendaraan  $kendaraan
     * @return \Illuminate\Http\Response
     */
    public function show(Kendaraan $kendaraan)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Kendaraan  $kendaraan
     * @return \Illuminate\Http\Response
     */
    public function edit(Request $request)
    {

        
        $validator = \Validator::make($request->all(), [
            'id_kendaraan' => 'required',
            'jenis_kendaraan' => 'required',
            'muatan_kendaraan' => 'required',
            // 'lokasi_awal' => 'required',
            // 'lokasi_akhir' => 'required'
        ]);
   
        if($validator->fails()){
            return $this->sendError('Validation Error.', $validator->errors());       
        }

        $kendaraan = Kendaraan::find($request->id_kendaraan);
        $kendaraan->jenis_kendaraan = $request->jenis_kendaraan;
        $kendaraan->muatan_kendaraan = $request->muatan_kendaraan;
        // $kendaraan->lokasi_awal = $request->lokasi_awal;
        // $kendaraan->lokasi_akhir = $request->lokasi_akhir;

        ;
        if($kendaraan->save()){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses mengubah data kendaraan',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Kendaraan  $kendaraan
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Kendaraan $kendaraan)
    {
        //
    }

    public function updateStatusKendaraaan(Request $request){
        $kendaraan =  Kendaraan::find($request->id);
        if($request->stat!=null){
            $kendaraan->stat = $request->stat;
            $msg = "Sukses mengubah setatus menjadi ". $request->stat;
            if($kendaraan->update()){
                $response = [
                    'status' => 'success',
                    'massage' => $msg,
                    'errors' => null,
                    'content' => null,
                ];
                return response()->json($response, 200);
            }
           
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Kendaraan  $kendaraan
     * @return \Illuminate\Http\Response
     */
    public function destroy(Kendaraan $kendaraan)
    {
        //
    }
}
