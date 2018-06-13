package kiva.client_03;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //ArrayList<String> pr_arraylist = new ArrayList<String>();

    private EditText editText;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    Button button2;
    String str;

    public Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        findViewById(R.id.button).setOnClickListener(mClickListener);
        button2=(Button) findViewById(R.id.button2);

        mSocket.connect().emit("start", "hi!");
        mSocket.on("jsonrows", result);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        listView = (ListView) findViewById(R.id.listView);
        //listView.setAdapter(arrayAdapter);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String data = editText.getText().toString();
                String JSON = str;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("end", data);
                    jsonObject.put("mid", JSON);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mSocket.emit("2", jsonObject);
            }
        });
    }


    private Emitter.Listener result = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final JSONObject[] arg = new JSONObject[1];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < args.length; i++) {
                        // PR_REVIEW pr = new PR_REVIEW(args[i]);
                        arg[0] = (JSONObject) args[i];
                        System.out.println("arg : " + arg[0]);
                    }
                    try {
                            String pos = arg[0].getString("pname");
                            arrayList.add(new String(pos));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    lists();
                    listView.setOnItemClickListener(mOnItemClickListener);
                }
            });
        }
    };
    private void lists() {
        System.out.println("listView : " + arrayList);
        listView.setAdapter(arrayAdapter);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String data = editText.getText().toString();
            String JSON = data;
            mSocket.emit("start", JSON);
        }
    };

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             str = arrayList.get(position);
            //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    };

}



