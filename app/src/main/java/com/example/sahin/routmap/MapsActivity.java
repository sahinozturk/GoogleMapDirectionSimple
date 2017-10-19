/*Go butonun clicklenme olayında:
EditTexte girilen veriyi aldık , Stringe çevirdik.
Bu isme sahip tüm konumları bulduk listemize ekledik.
Listemizin ilk elemanı bizim adresimiz oldu.
Bu adresin enlem ve boylam bilgilerini aldık.
Kameramızı oraya yönelttik ve oraya bir marker ekledik.
* */

package com.example.sahin.routmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
private final static int REQUEST_lOCATION=90;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Harita üzerinde zoom hareketi yapmamıza saglayacak olan kısım
        ZoomControls zoom=(ZoomControls)findViewById(R.id.zoom);

        // bu kısımda zoom out override ederek özelliğimizi çagırıyoruz
        zoom.setOnZoomOutClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        // bu kısımda zoom in override ederek özelliğimizi çagırıyoruz
         zoom.setOnZoomInClickListener(new View.OnClickListener()
        {
            public  void onClick(View view)
            {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });


        // getMapType() metodunu kullanarak Uydunun görünümünü ögreniyoruz
        // Öğrendiğimiz Bİlgiye göre setMapType() metodunu kullanarak görünümü
        // Değiştirceğiz
        // bu işlemleri gerçekleştiriken setOnClickListener metodunu kullanıyoruz

        //ilk önce bu işlemi yapmasın için oluşturdugumuz butonu çagırıyoruz
        final Button btn_MapType=(Button) findViewById(R.id.btn_Sat);

        // Ardından tıklamalara ulaşabilmek için setOnClickLİstener metodunu çağrıyoruz
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gelen tıklama sonrası harita görünümünü sorgulayarak değiştirme işlemini gerçekleştiriyoruz
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
                {
                    // Eğer şuanki durumu normal ise uydu görünümüyle değiştir dedik
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    //Değişiklik yapmamızı saglayan butonun yazısını değiştiriyoruz
                    btn_MapType.setText("NORMAL");
                }
                else
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText("UYDU");
                }


            }
        });

        // GO butonunu basıldığı zaman neler olacağını bu kısımda inceleyeceğiz
        Button btnGo=(Button) findViewById(R.id.btn_Go);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Gitmek istediğim yerin adını yazdığımız edittext
                //bölmünden bilgileri çekeceğiz
                EditText etLocation=(EditText)findViewById(R.id.et_location);

               // etLocation olarak aldığımız değerimizi Stringe çevirerek aldık
                String location=etLocation.getText().toString();

                // Bu kısımda kullanıcımızın null bir ifadeyi aratmaya kalkmaması
                // ve buşluk aratması yapamaması için bir if koşulu oluşturduk
                //Yani kullanıcı bir veri girişi yaptıysa arama yapılsın istiyoruz

                if(location!=null && !location.equals(""))
                {
                    //Adres bilgilerimizi tutacağımız bir adres arrayi oluşturuyoruz
                    List<Address> addressList=null;

                    //Geocoder sınıfın bize geocoder adında bir nesne oluşturuyor
                    //buradaki amacımız getFromLocationName() metodu ile edittexte
                    //Girilen tüm sonuçları alabilmektir
                    Geocoder geocoder=new Geocoder(MapsActivity.this);

                    try
                    {
                        // aldığımız bu adres bilgilerini oluşturduğumuz
                        //addresslite ekliyoruz
                        addressList=geocoder.getFromLocationName(location,1);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    // bu kısımda addressListin içindeki adreslerden
                    // ilk dönen adresi Adress tipinde bir nesneye atadık
                    Address address=addressList.get(0);

                    //Bu kısım gidecemiz adresin bilgilerini koyduğumuz alan
                    //ilk olarak Latitude ()  adresin enlem bilgisini
                    //longitude() ise boylam bilgisi alır
                    //get metoduyla bu bilgileri alarak latlng ye atadık ve adres bilgilerine
                    //sahip olmuş olduk
                    LatLng latLng=new LatLng(address.getLatitude(),
                            address.getLongitude());

                    //addMarker metodu bizim hedef noktamıza maker(işaretleyici)
                    //eklememizi saglıyor.
                    //MarkerOptions ile ise pozisyon ve başlık gibi görünebilinecek
                    //Özellikleri ekleyebiliyoruz
                    //Adres bilgilerini yani pozisyonu latLng ye atmıştık
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title("Tam olarak burası "+location));


                    //animateCamera yöntemi bizim konumumuzu değiştirmemizi sağlar
                    //içindeki metotlar sayesinde hedef noktaya yapılacak zoom hareketlerini
                    // ayarlayabiliriz
                    //Hedefe gitmek için kamerayı kesin hareket etmeliyiz
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));


                }

            }
        });


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            mMap.setMyLocationEnabled(true);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_lOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_lOCATION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Kullanıcı konum iznini vermedi", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}