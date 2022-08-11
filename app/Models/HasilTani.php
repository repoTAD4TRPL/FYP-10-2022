<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HasilTani extends Model
{
    use HasFactory;

    protected $table = "hasil_tani";
    
    protected  $primaryKey  = "id_hasil_panen";

    protected $fillable = [
        'id_petani',
        'berat_barang',
        'harga',
        'jenis_hasil_tani',
        'tanggal_post',
        'img'
    ];
}
