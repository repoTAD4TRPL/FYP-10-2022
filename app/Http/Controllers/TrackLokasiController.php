<?php

namespace App\Http\Controllers;

use App\Models\TrackLokasi;
use Illuminate\Http\Request;
use DB;
use Carbon;

class TrackLokasiController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index($id)
    {
        $listTrack = DB::table('track_lokasi')
        ->join('order_kendaraan', 'track_lokasi.id_order', '=','order_kendaraan.id_order')
        ->where('order_kendaraan.id_order', $id)
        ->select('track_lokasi.*')
        ->get();

        $response = [
            'status' => 'success',
            'massage' => 'Sukses mengambil lokasi',
            'data' => $listTrack,
            'content' => null,
        ];
        return response()->json($response, 200);
    }

    public function store(Request $request)
    {
        $validator = \Validator::make($request->all(), [
            'id_order' => 'required',
            'lokasi_saat_ini' => 'required',
            'lan' => 'required',
            'lat' => 'required',
            'catatan' => 'required'
        ]);


        $trackLokasi = [
            'id_order' => $request->id_order,
            'lokasi_saat_ini' => $request->lokasi_saat_ini,
            'lan' => $request->lan,
            'lat' => $request->lat,
            'catatan' => $request->catatan,
            'created_at' => Carbon\Carbon::now()
        ];

        $queryInsert = TrackLokasi::insert($trackLokasi);

        if($queryInsert){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses menambahkan lokasi',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }else{
            $response = [
                'status' => 'success',
                'massage' => 'Batal menambahkan lokasi',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }

}
