<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class OrderKendaraan extends Model
{
    use HasFactory;

    protected $table = "order_kendaraan";

    protected  $primaryKey  = "id_order";

    protected $fillable = [
        'alamat',
        'id_kendaraan',
        'id_petani',
        'status_pembayaran',
        'bukti_pembayaran',
        'berat_bawaan',
        'harga',
        'lokasi_awal',
        'lokasi_tujuan',
        'status',
        'pengaju',
        'status_pengajuan',
        'bukti_selesai',
        'konfirmasi_selesai',
        'catatan',
        'tanggal_penjmputan',
        'durasi_pengataran',
        'waktu_berangkat',
        'waktu_selesai',
        'harga_awal'
    ];
}
