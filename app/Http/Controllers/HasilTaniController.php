<?php

namespace App\Http\Controllers;

use App\Models\HasilTani;
use Illuminate\Http\Request;
use DB;

class HasilTaniController extends BaseController
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $hasilTani = DB::table('hasil_tani')
        ->join('user', 'hasil_tani.id_petani','=','user.id')
        ->orderBy('hasil_tani.id_hasil_panen', 'DESC')
        ->get();

        $response = [
            'status' => 'success',
            'massage' => 'semua hasil tani',
            'errors' => null,
            'content' => null,
            'data' => $hasilTani,
        ];
        return response()->json($response, 200);
    }

    public function getDataByiD($id){
        $hasilTani = DB::table('hasil_tani')
            ->join('user', 'hasil_tani.id_petani','=','user.id')
            ->where('hasil_tani.id_petani', $id)
            ->orderBy('hasil_tani.id_hasil_panen', 'DESC')
            ->get();

        // if($hasilTani>0){
            $response = [
                'status' => 'success',
                'massage' => 'semua hasil tani anda',
                'errors' => null,
                'content' => null,
                'data' => $hasilTani,
            ];
            return response()->json($response, 200);
        // }
    }

    public function store(Request $request)
    {
        $validator = \Validator::make($request->all(), [
            'id_petani' => 'required',
            'berat_barang' => 'required',
            'harga' => 'required',
            'jenis_hasil_tani' => 'required',
            'img' => 'required'
        ]);
   
        if($validator->fails()){
            return $this->sendError('Validation Error.', $validator->errors());       
        }

        $fileName = time().$request->file('img')->getClientOriginalName();

        $hasilTani = [
            'id_petani' => $request->id_petani,
            'berat_barang' => $request->berat_barang,
            'harga' => $request->harga,
            'jenis_hasil_tani' => $request->jenis_hasil_tani,
            'img' => $fileName
        ];

        $queryInsert = DB::table('hasil_tani')->insert($hasilTani);

        if($queryInsert){
            $request->file('img')->move(public_path('uploads'), $fileName);
            $response = [
                'status' => 'success',
                'massage' => 'Sukses menambahkan hasil tani',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }else{
            $response = [
                'status' => 'success',
                'massage' => 'Batal menambahkan hasil tani',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }

    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\HasilTani  $hasilTani
     * @return \Illuminate\Http\Response
     */
    public function show(HasilTani $hasilTani)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\HasilTani  $hasilTani
     * @return \Illuminate\Http\Response
     */
    public function edit(HasilTani $hasilTani)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\HasilTani  $hasilTani
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request)
    {
        $hasilTani = HasilTani::find($request->id);
        $fileName = "";
        if($request->file('img')!=null){
            $fileName = time().$request->file('img')->getClientOriginalName();
            $request->file('img')->move(public_path('uploads'), $fileName);
            $hasilTani->img = $fileName;
        }

        if($request->berat_barang!=null){
            $hasilTani->berat_barang = $request->berat_barang;
        }

        if($request->harga!=null){
            $hasilTani->harga = $request->harga;
        }

        if($request->jenis_hasil_tani!=null){
            $hasilTani->jenis_hasil_tani = $request->jenis_hasil_tani;
        }

        if($hasilTani->update()){
            $response = [
                'status' => 'ok',
                'massage' => 'Sukses mengupdate produk',
                'errors' => null,
                'content' => $fileName
            ];
            return response()->json($response, 200);
        }
        
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\HasilTani  $hasilTani
     * @return \Illuminate\Http\Response
     */
    public function destroy(Request $request)
    {
        $hasilTani = HasilTani::where('id_hasil_panen',$request->id_hasil_panen)->delete();

        if($hasilTani){$response = [
                    'status' => 'ok',
                    'massage' => 'Sukses menghapus hasil tani',
                    'errors' => null,
                    'content' => null,
                ];
                return response()->json($response, 200);
        }
    }
}
