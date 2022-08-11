<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TrackLokasi extends Model
{
    use HasFactory;

    protected $table = "track_lokasi";

    protected $fillable = [
        'id_order',
        'lokasi_saat_ini',
        'lan',
        'lat',
        'catatan'
    ];

}
