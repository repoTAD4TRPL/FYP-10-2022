<?php

namespace App\Http\Controllers;

use App\Models\OrderKendaraan;
use Illuminate\Http\Request;
use App\Models\Kendaraan;
use App\Models\CatatanOrder;
use DB;

class OrderKendaraanContoller extends BaseController
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        
    }

    public function getDataById($id, $role){
        $data;
        if($role=="petani"){
            $data = DB::table('order_kendaraan')
            ->join('user as petani', 'order_kendaraan.id_petani','=','petani.id')
            ->join('kendaraan', 'order_kendaraan.id_kendaraan', '=', 'kendaraan.id_kendaraan')
            ->join('user as driver', 'kendaraan.id_supir', '=', 'driver.id')
            ->where('order_kendaraan.id_petani', $id)
            ->select('order_kendaraan.*', 'kendaraan.*', 'driver.username as username_supir', 'driver.no_telepon as telepon', 'driver.nama as nama_supir','driver.no_tf as no_tf')
            ->orderBy('order_kendaraan.id_order', 'DESC')
            ->get();

        }else if($role=="supir"){
            $data = DB::table('order_kendaraan')
            ->join('kendaraan', 'order_kendaraan.id_kendaraan', '=', 'kendaraan.id_kendaraan')
            ->join('user as petani', 'order_kendaraan.id_petani', '=', 'petani.id')
            ->where('kendaraan.id_supir', $id)
            ->select('order_kendaraan.*', 'kendaraan.*', 'petani.username as username_petani', 'petani.no_telepon as telepon', 'petani.nama as nama_petani')
            ->orderBy('order_kendaraan.id_order', 'DESC')
            ->get();
        }

        $response = [
            'status' => 'success',
            'massage' => 'semua orderan',
            'errors' => null,
            'content' => null,
            'data' => $data
        ];
        return response()->json($response, 200);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function storeOrder(Request $request)
    {
        $validator = \Validator::make($request->all(), [
            'alamat' => 'required',
            'id_kendaraan' => 'required',
            'id_petani' => 'required',
            'status_pembayaran' => 'required',
            'bukti_pembayaran' => 'required',
            'berat_bawaan' => 'required',
            'harga' => 'required',
            'status' => 'required',
            'lokasi_awal' => 'required',
            'lokasi_tujuan' => 'required',
            'durasi_pengataran' => 'required',
            'tanggal_penjmputan' => 'required'
        ]);
   
        if($validator->fails()){
            return $this->sendError('Validation Error.', $validator->errors());       
        }

        $input = $request->all();
        $order = OrderKendaraan::create($input);

        if($order){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses memesan kendaraan',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }
    
    public function uploadBuktiOrder(Request $request){
        $fileName = time().$request->file('img')->getClientOriginalName();
        $request->file('img')->move(public_path('uploads'), $fileName);

        $order =  OrderKendaraan::find($request->id);
        
        $order->bukti_pembayaran = $fileName;

        $order->status_pembayaran = $request->bukti_pembayaran;


        $order->save();


        if($order->save()){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses Mengupload Bukti Pembayaran',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }

    public function uploadBuktiSelesai(Request $request){
        $fileName = time().$request->file('img')->getClientOriginalName();
        $request->file('img')->move(public_path('uploads'), $fileName);

        $order =  OrderKendaraan::find($request->id);
        
        $order->bukti_selesai = $fileName;

        $order->konfirmasi_selesai = $request->konfirmasi_selesai;


        $order->save();


        if($order->save()){
            $response = [
                'status' => 'success',
                'massage' => 'Sukses Mengupload Bukti Pembayaran',
                'errors' => null,
                'content' => null,
            ];
            return response()->json($response, 200);
        }
    }

    public function negiotateOrder(Request $request){
        $order = OrderKendaraan::find($request->id);
        
        //Mmebuat status ketiak dipesan dan selesai
        $kendaraan =  Kendaraan::find($order->id_kendaraan);

        
        if($request->konfirmasi_selesai!=null){
            $order->konfirmasi_selesai = $request->konfirmasi_selesai;
            $msg = "Proses orderan sukses di ".$request->konfirmasi_selesai;
        }


        if($request->status_pembayaran!=null){
            $order->status_pembayaran = $request->status_pembayaran;
            $msg = "Bukti orderan sukses di ".$request->status_pembayaran;
        }

        if($request->status_pengajuan!=null){
            $order->status_pengajuan = $request->status_pengajuan;
        }

        if($request->status=="Di terima supir" || $request->status=="Di terima petani" ){
            $order->bukti_pembayaran = "proses";
            $total_muatan = ($kendaraan->muatan_terpenuhi+$request->muatan_terpenuhi); 
            $kendaraan->muatan_terpenuhi = $total_muatan;
            if($total_muatan>=$kendaraan->muatan_kendaraan){
                $kendaraan->stat = "Dalam Pesanan";
            }
        }else if($request->status=="tolak"){
            
        }else if($request->status=="negosiasi"){

            if($order->harga_awal==0){
                $order->harga_awal = $order->harga;
            }
            $id=0;
            if($request->pengaju =="supir"){
                $id = $order->id_kendaraan;
            }else if($request->pengaju=="petani"){
                $id = $order->id_petani;
            }

            CatatanOrder::create([
                'id_order_kendaraan'=>$order->id_order,
                'harga'=> $request->harga,
                'pengaju'=> $request->pengaju,
                'id_pengaju' => $id,
                'catatan' => $request->catatan
            ]);
            
            $order->harga = $request->harga;
            // $order->harga_sebelum = $order->harga;
            $order->pengaju = $request->pengaju;
            $order->catatan = $request->catatan;

            $msg = "Proses orderan sukses di tawar";

        }else if($request->status=="Selesai"){
            $total_muatan = ($kendaraan->muatan_terpenuhi-$order->berat_bawaan);
            $kendaraan->muatan_terpenuhi = $total_muatan;
            if($kendaraan->muatan_terpenuhi<$kendaraan->muatan_kendaraan){
                $kendaraan->stat = "Tersedia";
            }
        }else if($request->status=="batalkan"){
            if($order->status!="Proses" && $order->status!="negosiasi"){
                $total_muatan = ($kendaraan->muatan_terpenuhi-$order->berat_bawaan);
                $kendaraan->muatan_terpenuhi = $total_muatan;
                if($kendaraan->muatan_terpenuhi<$kendaraan->muatan_kendaraan){
                    $kendaraan->stat = "Tersedia";
                }
            }
        }

        if($request->status!=null){
            $order->status = $request->status;
            $msg = "Proses orderan sukses di ".$request->status;
        }

        $kendaraan->save();
        $order->save();

        $response = [
            'status' => 'success',
            'massage' => $msg,
            'errors' => null,
            'content' => null,
        ];
        return response()->json($response, 200);

        
    }

    public function getCatatan($id){
        
        $catatanSupir = DB::table('catatan_order')
        ->where('catatan_order.pengaju', 'supir')
        ->join('kendaraan','kendaraan.id_kendaraan','=','catatan_order.id_pengaju')
        ->join('user', 'kendaraan.id_supir', '=', 'user.id')
        ->where('catatan_order.id_order_kendaraan', '=', $id)
        ->select('catatan_order.*', 'user.nama', 'user.poto_profil')
        ->get();

        $catatanPetani = DB::table('catatan_order')
        ->where('catatan_order.pengaju', 'petani')
        ->join('user','user.id','=','catatan_order.id_pengaju')
        ->where('catatan_order.id_order_kendaraan', '=', $id)
        ->select('catatan_order.*', 'user.nama', 'user.poto_profil')
        ->get();

        $data = array_merge((array)json_decode($catatanSupir), (array) json_decode($catatanPetani));
  
        ksort($data);
        $response = [
            'status' => 'success',
            'massage' => 'Riwayat catatan',
            'data' => $data
        ];
        return response()->json($response, 200);

    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\OrderKendaraan  $orderKendaraan
     * @return \Illuminate\Http\Response
     */
    public function show(OrderKendaraan $orderKendaraan)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\OrderKendaraan  $orderKendaraan
     * @return \Illuminate\Http\Response
     */
    public function edit(OrderKendaraan $orderKendaraan)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\OrderKendaraan  $orderKendaraan
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, OrderKendaraan $orderKendaraan)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\OrderKendaraan  $orderKendaraan
     * @return \Illuminate\Http\Response
     */
    public function destroy(OrderKendaraan $orderKendaraan)
    {
        //
    }

}
