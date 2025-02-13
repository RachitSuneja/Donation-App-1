package android.example.donationapp.Fragment;

import android.example.donationapp.Adapters.NGOMyEventAdapter;
import android.example.donationapp.Model.EventClass;
import android.example.donationapp.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NGOMyEventsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<EventClass> userList = new ArrayList<>();;
    RelativeLayout eventCard;

    String eventImage, eventDescription, eventTitle, eventLocation, etime, edate, eventTime;
    String econtact = "null", eEmail = "null";
    String UID;

    FirebaseUser firebaseUser ;

    FirebaseFirestore firebaseFirestore ;

    CollectionReference collectionReference ;
    NGOMyEventAdapter ngoMyEventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_ngo_my_events, container, false);

        recyclerView = view.findViewById(R.id.ngo_myEvent_recycler);

        eventCard = view.findViewById(R.id.myEvent_card);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        collectionReference  = firebaseFirestore.collection("Events").document(firebaseUser.getUid()).collection("random");
        ngoMyEventAdapter = new NGOMyEventAdapter(userList , getContext());

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.e("Inside onSuccess", "onSuccess: " );
                userList.clear();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    eventImage = documentSnapshot.getString("eImageUrl");
                    eventTitle = documentSnapshot.getString("eTitle");
                    eventDescription = documentSnapshot.getString("eDescription");
                    eventLocation = documentSnapshot.getString("eAddress");
                    etime = documentSnapshot.getString("eTime");
                    edate = documentSnapshot.getString("eDate");
                    UID = documentSnapshot.getString("uid");
                    String ngoName = documentSnapshot.getString("ngoName");
                    String generatedString = documentSnapshot.getString("generatedString");
                    Log.e("Inside For loop ", "" + ngoName);

                    if(firebaseUser.getUid().equalsIgnoreCase(UID))
                    {
                        userList.add(new EventClass(eventTitle, edate, etime, eventDescription, eventImage, eventLocation, econtact, eEmail, UID, ngoName , generatedString));
                        Log.e("Event Added", "EventTitle"+eventTitle.toString());
                    }
                }

                ngoMyEventAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ngoMyEventAdapter);

//        eventCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(getContext(), EventDetailActivity.class);
//                startActivity(intent);
//
//            }
//        });


        return view;
    }
}