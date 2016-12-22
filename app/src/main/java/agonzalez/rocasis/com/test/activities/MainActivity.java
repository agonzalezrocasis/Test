package agonzalez.rocasis.com.test.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import agonzalez.rocasis.com.test.R;
import agonzalez.rocasis.com.test.fragments.MessageHistoryListFragment;
import agonzalez.rocasis.com.test.fragments.MessageHistoryListFragmentListener;

public class MainActivity extends AppCompatActivity {

    private MessageHistoryListFragmentListener fragmentListener;
    ToggleButton mToggleButton;
    ImageView mImageViewBulb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageHistoryListFragment fragment = (MessageHistoryListFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.fragmentList);

        fragmentListener = (MessageHistoryListFragmentListener) fragment;
        fragment.setRetainInstance(true);

        mToggleButton = (ToggleButton) findViewById(R.id.mToggleButton);
        mImageViewBulb = (ImageView) findViewById(R.id.bulbImageView);

        final MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://iot.eclipse.org:1883", "testIoT");

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Toast.makeText(MainActivity.this, "Connection was lost!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this, "Message arrived: " + ": " + new String(message.getPayload()),
                        Toast.LENGTH_SHORT).show();
                fragmentListener.addToList(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Toast.makeText(MainActivity.this, "Delivery complete!", Toast.LENGTH_SHORT).show();
            }
        });

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    try {
                        mqttAndroidClient.connect(null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Toast.makeText(MainActivity.this, "Connection Success!", Toast.LENGTH_SHORT).show();
                                try {
                                    mqttAndroidClient.subscribe("IoTv1", 0);
                                } catch(MqttException ex) {

                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Toast.makeText(MainActivity.this, "Connection Failure!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch(MqttException ex) {
                        System.out.println(ex);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            //Do something
            return true;
        } else if (id == R.id.email) {
            //Do something
            return true;
        } else if (id == R.id.search) {
            //Do something
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
