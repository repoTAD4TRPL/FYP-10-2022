<?php

use App\Http\Controllers\AuthController;

Route::group([
    'middleware' => 'api',
    'prefix' => 'auth'
], function () {
    Route::post('login', 'AuthController@login');
    Route::post('signup', 'AuthController@signup');
    Route::get('signup/activate/{token}', 'AuthController@signupActivate');

    Route::group([
        'middleware' => 'auth:api',
    ], function() {
        Route::get('logout', [AuthController::class, 'logout']);
        Route::get('user', [AuthController::class, 'user']);
    });
});
