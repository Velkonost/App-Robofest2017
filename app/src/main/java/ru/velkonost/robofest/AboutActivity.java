package ru.velkonost.robofest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import ru.velkonost.robofest.adapters.AboutAdapter;

import static ru.velkonost.robofest.managers.Initializations.changeActivityCompat;
import static ru.velkonost.robofest.managers.Initializations.hasConnection;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String textHistory;
    private String text;


    private Document[] doc = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("О фестивале");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!hasConnection(AboutActivity.this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
            builder.setTitle("Ошибка")
                    .setMessage("Отсутствует интернет-соединение!")
                    .setCancelable(false)
                    .setNegativeButton("Хорошо",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            GetHtml getHtml = new GetHtml();
            getHtml.execute();
        }
    }



//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    public void openMain (View view) {

        if (!hasConnection(AboutActivity.this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
            builder.setTitle("Ошибка")
                    .setMessage("Отсутствует интернет-соединение!")
                    .setCancelable(false)
                    .setNegativeButton("Хорошо",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

            return;
        }

        final Intent finalNextIntent = new Intent("ru.velkonost.Browser");
        finalNextIntent.putExtra("site", 2);
        finalNextIntent.setData(Uri.parse(
                "https://www.robofestomsk.ru/index.html"
        ));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * Обновляет страницу.
                 * {@link Initializations#changeActivityCompat(Activity, Intent)}
                 * */
                changeActivityCompat(AboutActivity.this, finalNextIntent);
            }
        }, 350);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent nextIntent = null;

        int id = item.getItemId();

        if (id == R.id.registration) {


            if (!hasConnection(AboutActivity.this)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setTitle("Ошибка")
                        .setMessage("Отсутствует интернет-соединение!")
                        .setCancelable(false)
                        .setNegativeButton("Хорошо",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

                nextIntent = new Intent(AboutActivity.this, AboutActivity.class);
            } else {

                nextIntent =
                        new Intent("ru.velkonost.Browser");
                nextIntent.putExtra("site", 1);
                nextIntent.setData(Uri.parse(
                        "https://docs.google.com/forms/d/e/1FAIpQLSfg7od0RMlO5CCML1MZB2dxVnS-3KG8rqTGZ2hitnVY2tdpxg/formResponse"
                ));
            }

        } else if (id == R.id.galery) {
            nextIntent = new Intent(AboutActivity.this, RecyclerView_Activity.class);
        } else if (id == R.id.about) {
            nextIntent = new Intent(AboutActivity.this, AboutActivity.class);
        } else if (id == R.id.organizers) {
            nextIntent = new Intent(AboutActivity.this, OrganizersActivity.class);
        } else if (id == R.id.contacts) {
            nextIntent = new Intent(AboutActivity.this, ContactsActivity.class);
        }


        final Intent finalNextIntent = nextIntent;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                changeActivityCompat(AboutActivity.this, finalNextIntent);
                finish();
            }
        }, 350);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetHtml extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... strings) {

            String dataURL = "http://www.robofestomsk.ru/o-festivale.html";

            try {
                doc[0] = Jsoup.connect(dataURL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return dataURL;
        }
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);

            List title = doc[0].select("article[class=box post]").select("p");

            textHistory = TextUtils.join(" ", title.subList(1, 2));
            text = TextUtils.join(" ", title.subList(5, 8));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textHistory = String.valueOf(Html.fromHtml(textHistory, Html.FROM_HTML_MODE_LEGACY));
                text = String.valueOf(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textHistory = String.valueOf(Html.fromHtml(textHistory));
                text = String.valueOf(Html.fromHtml(text));
            }

            RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerViewAbout);
            rv.setLayoutManager(new LinearLayoutManager(AboutActivity.this));
            rv.setAdapter(new AboutAdapter(textHistory, text, AboutActivity.this));
        }
    }
}
