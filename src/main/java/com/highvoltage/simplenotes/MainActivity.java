package com.highvoltage.simplenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;





public class MainActivity extends AppCompatActivity
{
    static ArrayList<String> notes =new ArrayList<>();
    static  ArrayAdapter arrayAdapter;


    @Override
    
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        //menuInflater.inflate(R.menu.lo, menu);
        return super.onCreateOptionsMenu(menu);


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() ==R.id.add_note)
        {
            Toast.makeText(MainActivity.this,"New Note Created Sucessfully",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(getApplicationContext(),NoteEditorActivity.class);
          startActivity(intent);

          return  true;
        }


        // Login With AuthMode
        if(item.getItemId() ==R.id.login)
        {
            Toast.makeText(MainActivity.this,"Login with AuthMode",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=(ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.highvoltage.simplenotes", Context.MODE_PRIVATE);
        HashSet<String>set=(HashSet<String>)sharedPreferences.getStringSet("notes",null);
        if(set==null)
        {
            notes.add("Example Note");
        }
        else
        {
            notes=new ArrayList<>(set);
        }

        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        //when we click that text it will go inside
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                Intent intent=new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteId",pos);
                startActivity(intent);
            }
        });
          // to delete a File
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l)
            {
                int itemToDelete=i;
                new AlertDialog.Builder(MainActivity.this)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setTitle("Are You Sure")
                       .setMessage("Do You Want To Delete ?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which)
                           {
                               notes.remove(itemToDelete);
                               arrayAdapter.notifyDataSetChanged();

                               Toast.makeText(MainActivity.this,"Note Deleted Sucessfully",Toast.LENGTH_SHORT).show();

                               SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.highvoltage.simplenotes", Context.MODE_PRIVATE);
                               HashSet<String> set=new HashSet(MainActivity.notes);
                               sharedPreferences.edit().putStringSet("notes",set).apply();
                           }
                       }
                       ).setNegativeButton("No",null)
                       .show();
                return true;
            }
        });
    }
}