package com.i.should.what.whatshouldi.SayPackage;

import android.os.AsyncTask;

import com.i.should.what.whatshouldi.FileUtilsSaySection;

/**
 * Created by ryan on 7.7.2015.
 */
public class ReadSayDataAsyncTask extends AsyncTask<ReadSayAsyncParams, Void, SayModel[]> {

    private ReadSayAsyncParams asyncParams;

    @Override
    protected SayModel[] doInBackground(ReadSayAsyncParams... params) {
        asyncParams = params[0];

        SayModel[] models = FileUtilsSaySection.readSaySection(asyncParams.context.get());
        return models;
    }

    @Override
    protected void onPostExecute(SayModel[] sayModels) {
        SayAdapter adapter = asyncParams.adapter.get();
        if(adapter !=null && sayModels!=null && sayModels.length>0)
        {
            for (SayModel s:sayModels){
                asyncParams.adapter.get().add(s);
            }

            asyncParams.adapter.get().notifyDataSetChanged();
        }
    }
}
