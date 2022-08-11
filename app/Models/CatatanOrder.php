<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class CatatanOrder extends Model
{
    use HasFactory;
    protected $table = "catatan_order";

    protected $fillable = [
        'id',
        'id_order_kendaraan',
        'pengaju',
        'harga',
        'catatan',
        'id_pengaju'
    ];
}
