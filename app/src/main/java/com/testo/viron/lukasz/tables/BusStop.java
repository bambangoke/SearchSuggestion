package com.testo.viron.lukasz.tables;

import com.orm.SugarRecord;

/**
 * Created by Lukas on 2015-05-20.
 */
public class BusStop extends SugarRecord<BusStop> {
    public String busStopName;

    public BusStop(String s){
        busStopName=s;
    }
    public BusStop(){}
}
