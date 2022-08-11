<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

//Controller
use App\Http\Controllers\AuthController;
use App\Http\Controllers\HasilTaniController;
use App\Http\Controllers\KendaraanController;
use App\Http\Controllers\OrderKendaraanContoller;
use App\Http\Controllers\TrackLokasiController;
use App\Http\Controllers\PelabuhanController;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::post('login', [AuthController::class, 'login']);
Route::post('register', [AuthController::class, 'register']);

Route::get('reset_password_without_token/{email}', [AuthController::class, 'validatePasswordRequest']);
Route::post('reset_password_with_token', [AuthController::class, 'resetPassword']);


Route::group(['prefix' => 'auth', 'middleware' => 'auth:sanctum'], function() {
    // manggil controller sesuai bawaan laravel 8
    Route::post('logout', [AuthController::class, 'logout']);
    // manggil controller dengan mengubah namespace di RouteServiceProvider.php biar bisa kayak versi2 sebelumnya
    Route::post('logoutall',  [AuthController::class, 'logoutall']);
});



Route::group(['prefix' => 'profil',  'middleware' => 'api'], function(){
    Route::post('update', [AuthController::class, 'updateProfil']);
});


//Edit Belum
Route::group(['prefix' => 'hasilTani',  'middleware' => 'api'], function(){
    Route::get('all', [HasilTaniController::class, 'index']);
    Route::get('user/{id}', [HasilTaniController::class, 'getDataByiD']);
    Route::post('hasil_tani/store',[HasilTaniController::class, 'store']);
    Route::post('hasil_tani/hapus',[HasilTaniController::class, 'destroy']);
    Route::post('hasil_tani/edit',[HasilTaniController::class, 'update']);
});

Route::group(['prefix' => 'kendaraan', 'middleware' => 'api'],  function(){
    Route::get('all/{lat}/{long}', [KendaraanController::class, 'index']);
    Route::post('store', [KendaraanController::class, 'store']);
    Route::post('edit', [KendaraanController::class,  'edit']);
    Route::post('updateStatusKendaraaan', [KendaraanController::class,  'updateStatusKendaraaan']);
});

Route::group(['prefix' => 'orderKendaraan', 'middleware'=>'api'], function(){
    Route::get('user/{id}/{role}', [OrderKendaraanContoller::class, 'getDataById']);
    Route::post('user/order/petani', [OrderKendaraanContoller::class, 'storeOrder']);
    Route::post('user/order/negotiate', [OrderKendaraanContoller::class, 'negiotateOrder']);
    Route::post('user/order/uploadBuktiOrder', [OrderKendaraanContoller::class, 'uploadBuktiOrder']);
    Route::post('user/order/uploadBuktiSelesai', [OrderKendaraanContoller::class, 'uploadBuktiSelesai']);
    Route::get('user/order/catatan/{id}', [OrderKendaraanContoller::class, 'getCatatan']);
});


Route::group(['prefix' => 'trackLokasi', 'middleware'=>'api'], function(){
    Route::get('byOrder/{id}', [TrackLokasiController::class, 'index']);
    Route::post('add', [TrackLokasiController::class, 'store']);
});

Route::group(['prefix'=> 'pelabuhan', 'middleware'=>'api'], function(){
    Route::get('all',[PelabuhanController::class, 'index']);
});