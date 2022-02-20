package com.bitp3453.bitis1g1.projectrequest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>  {

    private Vector<Request> requests;
    private Context context;

    public RequestAdapter(Context context, Vector<Request> requests) {
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater.from(context).inflate(R.layout., parent, false);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout., parent, false);
        view.setOnClickListener(v -> {
            TextView name = v.findViewById(R.id.);
            TextView quantity = v.findViewById(R.id.);
            TextView description = v.findViewById(R.id.);
            TextView phonenumber = v.findViewById(R.id.);
            TextView requestid = v.findViewById(R.id.);

            Intent intent = new Intent(v.getContext(), RequestDetailActivity.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("quantity", quantity.getText().toString());
            intent.putExtra("description", description.getText().toString());
            intent.putExtra("phonenumber", phonenumber.getText().toString());
            intent.putExtra("requestid", requestid.getText().toString());

            context.startActivity(intent);
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = requests.get(position);

        holder.txtname.setText(request.getName());
        holder.txtdescription.setText(request.getDescription());
        holder.txtphonenumber.setText(request.getPhonenumber());
        holder.txtrequestid.setText(request.getRequestid());
        holder.txtquantity.setText(request.getQuantity());

        GoogleMap thisMap = holder.mapCurrent;
        if (thisMap != null) {
            thisMap.addMarker(new MarkerOptions().position(new LatLng(request.getCoordinate())));
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        private final TextView txtname, txtphonenumber, txtrequestid, txtquantity, txtdescription;
        private GoogleMap mapCurrent;
        MapView map;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            map = itemView.findViewById(R.id.);
            if (map != null) {
                //map.onCreate(null);
                // map.onResume();
                //map.getMapAsync(this);
            }

            txtname = itemView.findViewById(R.id.);
            txtdescription = itemView.findViewById(R.id.);
            txtphonenumber = itemView.findViewById(R.id.);
            txtrequestid = itemView.findViewById(R.id.);
            txtquantity = itemView.findViewById(R.id.);
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mapCurrent = googleMap;
        }

    }

}
