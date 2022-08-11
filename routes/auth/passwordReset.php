<?php

use App\Http\Controllers\PasswordResetController;

Route::group([
    // 'namespace' => 'Controllers',
    'middleware' => 'api',
    'prefix' => 'password'
], function () {
    Route::post('create', [PasswordResetController::class, 'create']);
    Route::get('find/{token}', [PasswordResetController::class, 'find']);
    Route::post('reset', [PasswordResetController::class, 'reset']);
});
