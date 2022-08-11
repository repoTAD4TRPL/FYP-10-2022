<?php

namespace App\Http\Controllers;

use App\Models\Pelabuhan;
use Illuminate\Http\Request;

class PelabuhanController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        
        dd(public_path());
        $pelabuhan = Pelabuhan::all();
        $response = [
            'status' => 'success',
            'massage' => 'Sukses mengambil lokasi',
            'data' => $pelabuhan,
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
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Pelabuhan  $pelabuhan
     * @return \Illuminate\Http\Response
     */
    public function show(Pelabuhan $pelabuhan)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Pelabuhan  $pelabuhan
     * @return \Illuminate\Http\Response
     */
    public function edit(Pelabuhan $pelabuhan)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Pelabuhan  $pelabuhan
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Pelabuhan $pelabuhan)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Pelabuhan  $pelabuhan
     * @return \Illuminate\Http\Response
     */
    public function destroy(Pelabuhan $pelabuhan)
    {
        //
    }
}
