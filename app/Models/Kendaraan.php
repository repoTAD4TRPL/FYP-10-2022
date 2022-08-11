<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Kendaraan extends Model
{
    use HasFactory;

    protected  $primaryKey = "id_kendaraan";

    protected $table = "kendaraan";

    protected $fillable = [
        'id_supir',
        'jenis_kendaraan',
        'muatan_kendaraan',
        'muatan_terpenuhi',
        'stat',
        'lokasi_awal',
        'lokasi_akhir'
    ];
}
