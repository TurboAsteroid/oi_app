package com.example.lvo.alertnotifications;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import android.support.v4.content.FileProvider;




public class MyAdapterFilesList extends RecyclerView.Adapter<MyAdapterFilesList.ViewHolder> {
    private List<String> mDataset;
    private String incidentId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_filename;
        private ViewHolder(View v) {

            super(v);
            text_filename = v.findViewById(R.id.filename);
        }
        public void bind(String message) {
            text_filename.setText(message);
        }
    }

    public MyAdapterFilesList(List<String> myDataset, String incident_id) {
        mDataset = myDataset;
        incidentId = incident_id;
    }

    @Override
    public MyAdapterFilesList.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fileslist, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final TextView filename = view.findViewById(R.id.filename);
                final String filepath = view.getContext().getExternalFilesDir(null) + File.separator + incidentId + File.separator + filename.getText().toString();
                final File file = new File(filepath);
                if(!file.isFile()) {
                    App.getApi().downloadFile(incidentId, filename.getText().toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            writeResponseBodyToDisk(response.body(), file);
                            openFile(file, view, filepath);
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //Произошла ошибка
                            Log.d("11111111111111", t.toString() );
                        }
                    });
                }
                openFile(file, view, filepath);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }
    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    public static void openFile(File file, View view, String filepath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(FileProvider.getUriForFile(view.getContext(), view.getContext().getApplicationContext().getPackageName() + ".provider", file), getMimeType(filepath));
        view.getContext().startActivity(intent);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, File futureStudioIconFile) {

        try {
            // todo change the file location/name according to your needs
            if(!futureStudioIconFile.getParentFile().exists()) {
                futureStudioIconFile.getParentFile().mkdirs();
            }


            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("11111111111111", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}