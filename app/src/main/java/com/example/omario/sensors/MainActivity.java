package com.example.omario.sensors;
import android.hardware.SensorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    float[] acceleration = new float[3];
    float[] magnetic = new float[3];
    float[] gyro = new float[3];



    TextView txt_ax ;
    TextView txt_ay ;
    TextView txt_az ;

    TextView txt_mx ;
    TextView txt_my ;
    TextView txt_mz ;

    TextView txt_gx ;
    TextView txt_gy ;
    TextView txt_gz ;

    TextView txt_timestamp ;

    ////// tro rm
    private SensorManager sm;
    private SensorManager mg;
    Sensor accelerometers;
    Sensor magnetics;


    List<Sensor> glob ;
    private SensorManager sensGlob ;


    public static BufferedWriter out ;
    private static Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_ax = (TextView) findViewById(R.id.txt_ax);
        txt_ay = (TextView) findViewById(R.id.txt_ay);
        txt_az = (TextView) findViewById(R.id.txt_az);

        txt_mx = (TextView) findViewById(R.id.txt_mx);
        txt_my = (TextView) findViewById(R.id.txt_my);
        txt_mz = (TextView) findViewById(R.id.txt_mz);

        txt_gx = (TextView) findViewById(R.id.txt_gx);
        txt_gy = (TextView) findViewById(R.id.txt_gy);
        txt_gz = (TextView) findViewById(R.id.txt_gz);

        txt_timestamp = (TextView) findViewById(R.id.txt_timestamp);


        gyro[0] =0.0f ;
        gyro[1] =0.0f ;
        gyro[2] =0.0f;

        sensGlob = (SensorManager) getSystemService(SENSOR_SERVICE) ;


        glob = sensGlob.getSensorList(Sensor.TYPE_ALL);

               for (Sensor s : glob) {
          Log.i("ssss",s.toString() + "\n");
                   sensGlob.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
        }


        try {
            createFileOnDevice(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addText("","timestamp   "+"ax     ay      az    " +"mx     my      mz   " +"gx     gy      gz"+"\n");

    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()){
            case  Sensor.TYPE_ACCELEROMETER :
                System.arraycopy(event.values, 0, acceleration, 0, 3);
                txt_ax.setText(String.valueOf(acceleration[0]));
                txt_ay.setText(String.valueOf(acceleration[1]));
                txt_az.setText(String.valueOf(acceleration[2]));

                break ;
            case Sensor.TYPE_MAGNETIC_FIELD :
                System.arraycopy(event.values, 0, magnetic, 0, 3);

                txt_mx.setText(String.valueOf(magnetic[0]));
                txt_my.setText(String.valueOf(magnetic[1]));
                txt_mz.setText(String.valueOf(magnetic[2]));

                break ;
            case Sensor.TYPE_GYROSCOPE :
                System.arraycopy(event.values, 0, gyro, 0, 3);

                txt_gx.setText(String.valueOf(gyro[0]));
                txt_gy.setText(String.valueOf(gyro[1]));
                txt_gz.setText(String.valueOf(gyro[2]));

                break ;

        }

        txt_timestamp.setText(String.valueOf(event.timestamp));




        addText("",event.timestamp+"\t"+
                acceleration[0] +"\t"+  acceleration[1] +"\t"+  acceleration[2] +"\t"+
                magnetic[0]     +"\t"+  magnetic[1]     +"\t"+  magnetic[2]      +"\t"+
                gyro[0]         +"\t"+  gyro[1]         +"\t"+  gyro[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private static void createFileOnDevice(Boolean append) throws IOException {
    /*
     * Function to initially create the log file and it also writes the time
     * of creation to file.
     */
        File root = Environment.getExternalStorageDirectory();

        Log.d("path",root.toString());

        if (root!=null && root.exists() && root.canWrite()) {
            File LogFile = new File(root, "LOG.txt");
            FileWriter LogWriter = new FileWriter(LogFile, append);
            out = new BufferedWriter(LogWriter);
        }
    }

    public static void addText(String tag, String message) {

        if(out==null){
            try {
                createFileOnDevice(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if(out==null)return;
            //    out.write("s-d: "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
            //   out.write(" \t ");
            out.write(tag.toUpperCase()+": "+message + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}