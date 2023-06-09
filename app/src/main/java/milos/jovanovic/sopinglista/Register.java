package milos.jovanovic.sopinglista;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String REGISTER_URL = LoginActivity.BASE_URL + "/users";
    private HttpHelper httpHelper;


    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Button button_register;
    Button home;
    EditText username, email, password;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View p = inflater.inflate(R.layout.fragment_register, container, false);
        button_register = p.findViewById(R.id.dugme_register);
        username = p.findViewById(R.id.user_register);
        email = p.findViewById(R.id.editText_gmail);
        password = p.findViewById(R.id.editText_pass);
        home = p.findViewById(R.id.home4);
        httpHelper = new HttpHelper();

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String em = email.getText().toString();
                String pass = password.getText().toString();
                /*
                DBHelper db = new DBHelper(getActivity(), DBHelper.DB_NAME, null, 1);
                if (db.doesUserExist(username.getText().toString())) {
                    Toast.makeText(getActivity(), "Registration successful!", Toast.LENGTH_SHORT).show();
                    db.insertUser(username.getText().toString(), email.getText().toString(), password.getText().toString());
                    Intent ii = new Intent(getActivity(), WelcomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username.getText().toString());
                    ii.putExtras(bundle);
                    startActivity(ii);
                } else {
                    Toast.makeText(getActivity(), "Registration failed!", Toast.LENGTH_SHORT).show();
                }
                */
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            JSONObject requestJSON = new JSONObject();
                            requestJSON.put("username", user);
                            requestJSON.put("password", pass);
                            requestJSON.put("email", em);
                            boolean jsonObject = httpHelper.postJSONObjectFromURL(REGISTER_URL,requestJSON);
                            if(jsonObject) {
                                Intent intent = new Intent(getActivity(),WelcomeActivity.class);
                                intent.putExtra("username",user);
                                intent.putExtra("email",em);
                                intent.putExtra("password",pass);
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), "ERROR REGISTER HTTP", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
        return p;
    }
}