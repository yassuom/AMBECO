package br.com.ambeco;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.ambeco.adapter.spinnerAdapter;
import br.com.ambeco.beans.LocalBean;
import br.com.ambeco.dao.LocalDAO;
import br.com.ambeco.helpers.LocaisHelper;
import br.com.ambeco.helpers.UserHelper;
import br.com.ambeco.utilitys.Utility;

public class CadastraLocalActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    public static final int CODIGO_GALERIA = 568;
    private LocaisHelper locaisHelper;
    private UserHelper userHelper;
    private String userChoosenTask;

    private long idCategoriaSelecionada;

    String[] ufList = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
                            "RS", "RO", "RR", "SC", "SP", "SE", "TO"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_local);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cadastra_local_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(Boolean.FALSE);

        Intent intent = getIntent();
        LocalBean localBean = (LocalBean) intent.getSerializableExtra("local");

        if(localBean != null) {
            locaisHelper.preencheFormulario(localBean);
        }


        locaisHelper = new LocaisHelper(this);
        userHelper = new UserHelper(this);

        //configura o Spinner
        configureSpinner();

        //configura o SeekBar
        configureSeekBar();

        //configura os RadioButton da tela
        configureRadioButtons();

        Button botaoFoto = (Button) findViewById(R.id.cadastra_local_botao_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_GALERIA)
                onSelectFromGalleryResult(data);
            else if (requestCode == CODIGO_CAMERA)
                onCaptureImageResult(data);
        }
    }

    /**
     * Método responsável por configurar o SeekBar
     * que sinaliza o nível de degradação do
     * local.
     */
    private void configureSeekBar() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.cadastra_local_nivel);
        final TextView textView = (TextView) findViewById(R.id.cadastra_local_nivel_text);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String textoSeekBar = String.format("%s/10", String.valueOf(progress));
                textView.setText(textoSeekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Método responsável por configurar o spinner
     * que o UF do local.
     */
    private void configureSpinner() {
        final Spinner spinner_uf = (Spinner) findViewById(R.id.cadastra_local_uf);

        spinnerAdapter adapter_uf = new spinnerAdapter(CadastraLocalActivity.this, android.R.layout.simple_list_item_1);
        adapter_uf.addAll(ufList);
        adapter_uf.add("UF");
        spinner_uf.setAdapter(adapter_uf);
        spinner_uf.setSelection(adapter_uf.getCount());
    }

    /**
     * Método que configura o comportamento dos
     * RadioButton da tela.
     */
    private void configureRadioButtons() {

        final RadioButton radioQueimada = (RadioButton) findViewById(R.id.cadastra_local_queimada);
        final RadioButton radioDeslizamento = (RadioButton) findViewById(R.id.cadastra_local_deslizamento);
        final RadioButton radioLixo = (RadioButton) findViewById(R.id.cadastra_local_lixo);
        final RadioButton radioDesmatamento = (RadioButton) findViewById(R.id.cadastra_local_desmatamento);

        final RadioButton[] listRadios = {radioQueimada,radioDeslizamento,radioLixo,radioDesmatamento};

        radioQueimada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdCategoriaSelecionada(1);
                disableRadioOnCheck(radioQueimada, listRadios);
            }
        });
        radioDeslizamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdCategoriaSelecionada(2);
                disableRadioOnCheck(radioDeslizamento, listRadios);
            }
        });
        radioLixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdCategoriaSelecionada(3);
                disableRadioOnCheck(radioLixo, listRadios);
            }
        });
        radioDesmatamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIdCategoriaSelecionada(4);
                disableRadioOnCheck(radioDesmatamento, listRadios);
            }
        });
    }

    /**
     * Método que checa se os RadioButton para alterar
     * os status de checado ou não conforme selecionado.
     *
     * @param clickedButton
     * @param listButton
     */
    private void disableRadioOnCheck(RadioButton clickedButton, RadioButton[] listButton) {
        for(RadioButton radio : listButton) {
            if(radio != clickedButton) {
                radio.setChecked(Boolean.FALSE);
            } else {
                radio.setChecked(Boolean.TRUE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastra_local, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                LocalBean local = locaisHelper.getLocal(userHelper.getUserId());

                LocalDAO dao = new LocalDAO(this);
                //if(aluno.getId() != null) {
                //    dao.alteraAluno(aluno);
                //} else {
                    dao.insertLocal(local);
                //}
                dao.close();

                Toast.makeText(CadastraLocalActivity.this, "Local "+ local.getDescricao() + " salvo!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
        final CharSequence[] items = { "Tirar Foto", "Galeria",
                "Cancelar" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CadastraLocalActivity.this);
        builder.setTitle("Adicionar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CadastraLocalActivity.this);

                if (items[item].equals("Tirar Foto")) {
                    userChoosenTask="Tirar Foto";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Galeria")) {
                    userChoosenTask="Galeria";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CODIGO_CAMERA);
    }

    private void galleryIntent()
    {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CODIGO_GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Tirar Foto"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Galeria"))
                        galleryIntent();
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri selectedImage = data.getData();
            String [] filePathColumn = { MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bm = null;

            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            locaisHelper.carregaImagem(bm, picturePath);
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        locaisHelper.carregaImagem(thumbnail, destination.getPath());
    }

    public long getIdCategoriaSelecionada() {
        return idCategoriaSelecionada;
    }

    public void setIdCategoriaSelecionada(long idCategoriaSelecionada) {
        this.idCategoriaSelecionada = idCategoriaSelecionada;
    }
}
