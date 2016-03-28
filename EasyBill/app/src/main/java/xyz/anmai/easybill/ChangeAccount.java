package xyz.anmai.easybill;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import entity.Account;
import util.DBManager;
import util.MyAdapter;

public class ChangeAccount extends AppCompatActivity {
    /**
     * 启动activity接口函数
     */
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, ChangeAccount.class);
        context.startActivity(intent);
    }

    public static void activityStart(Context context, AccountActivity.AccountItem accountItem) {
        Intent intent = new Intent(context, ChangeAccount.class);
        intent.putExtra("db_id", accountItem.db_id);
        context.startActivity(intent);
    }

    private static final String TAG = "ChangeActivity";
    private static int db_id;
    private static boolean modify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);

        Intent intent = getIntent();
        db_id = intent.getIntExtra("db_id", 0);
        if (db_id != 0) {
            modify = true;
        }

        //获取FragmentManager对象
        FragmentManager manager = getFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //添加Fragment对象
        transaction.add(R.id.account_change_fragment_holder, new AddAccount(), null);
        //提交
        transaction.commit();
    }

    public static class AddAccount extends Fragment {
        private ArrayList<Account> data = null;
        private MyAdapter adapter = null;

        private AlertDialog alert = null;
        private AlertDialog.Builder builder = null;

        private Spinner spinner = null;//spinner
        private String spinner_text = null;//spinner中item的text
        private int spinner_ico;//spinner中item的图标
        private EditText num = null;
        private EditText money = null;
        private Button commit = null;

        private DBManager dbManager = null;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_add_account, container, false);

            dbManager = new DBManager(getActivity());
            if (dbManager.db == null) {
                Log.e(TAG, "连接数据库失败！！！");
            } else {
                Log.e(TAG, "连接数据库成功！");
            }
            spinner = (Spinner) view.findViewById(R.id.account_spinner_add);
            num = (EditText) view.findViewById(R.id.account_num_add);
            money = (EditText) view.findViewById(R.id.account_money_add);
            commit = (Button) view.findViewById(R.id.add_account_commit_button);
            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "ico=" + spinner_ico + ";spiner=" + spinner_text + ";num=" + num.getText().toString() + ";money=" + money.getText().toString());
                    if (num.getText().toString().equals("") || money.getText().toString().equals("")) {
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.ic_launcher)
                                .setTitle("请将信息填写完整")
                                .setMessage("ico=" + spinner_ico + ";\nspiner=" + spinner_text + ";\nnum=" + num.getText().toString() + ";\nmoney=" + money.getText().toString())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    } else {
                        if (dbManager.db != null) {
                            try {
                                dbManager.db.execSQL("insert into account values(null, '666666', '" + spinner_text + "', '" + num.getText().toString() + "','" + money.getText().toString() + "')");
                                Log.e(TAG, "添加到数据库成功！");
                                Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                                Log.e(TAG, "添加到数据库失败！！！");
                            }
                        }
                    }
                }
            });

            //绑定视图数据
            bindViews();

            if (modify) {
                Cursor c = null;
                try {
                    c = dbManager.db.rawQuery("select * from account where id = " + db_id, null);
                    if (c.moveToNext()) {
                        String _cate = c.getString(c.getColumnIndex("cate"));
                        String _num = c.getString(c.getColumnIndex("num"));
                        String _money = c.getString(c.getColumnIndex("money"));
                        switch (_cate) {
                            case "支付宝":
                                spinner.setSelection(0);
                                break;
                            case "建行卡":
                                spinner.setSelection(1);
                                break;
                            case "工行卡":
                                spinner.setSelection(2);
                                break;
                            case "VISA":
                                spinner.setSelection(3);
                                break;
                            case "其他":
                                spinner.setSelection(4);
                                break;
                            default:
                                spinner.setSelection(4);
                                break;
                        }
                        num.setText(_num);
                        money.setText(_money);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert = null;
                        builder = new AlertDialog.Builder(getActivity());
                        alert = builder.setIcon(R.mipmap.ic_launcher)
                                .setTitle("确认")
                                .setMessage("是否修改！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (dbManager.db != null) {
                                            try {
                                                dbManager.db.execSQL("update account set cate = ?,num = ?,money = ? where id = ?", new Object[]{spinner_text, num.getText().toString(), money.getText().toString(), db_id});
                                                Log.e(TAG, "更新数据库成功！");
                                                Toast.makeText(getActivity(), "更新成功！", Toast.LENGTH_SHORT).show();
                                                getActivity().finish();
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                                Log.e(TAG, "更新数据库失败！！！");
                                            }
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "取消修改", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    }
                                }).create();             //创建AlertDialog对象
                        alert.show();
                    }
                });
            }
            return view;
        }

        //为spinner绑定视图
        private void bindViews() {
            data = new ArrayList<>();

            data.add(new Account(R.drawable.alipay, "支付宝"));
            data.add(new Account(R.mipmap.ic_launcher, "建行卡"));
            data.add(new Account(R.mipmap.ic_launcher, "工行卡"));
            data.add(new Account(R.drawable.visa, "VISA"));
            data.add(new Account(R.mipmap.ic_launcher, "其他"));

            adapter = new MyAdapter<Account>(data, R.layout.item_spin_account_all) {
                @Override
                public void bindView(ViewHolder holder, Account obj) {
                    holder.setImageResource(R.id.img_icon, obj.getaIcon());
                    holder.setText(R.id.txt_name, obj.getaName());
                }
            };
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinner_ico = (view.findViewById(R.id.img_icon)).getId();
                    spinner_text = ((TextView) view.findViewById(R.id.txt_name)).getText().toString();
                    Log.e(TAG, "spinner_text = " + spinner_text);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (dbManager.db != null) {
                dbManager.close();
                Log.e(TAG, "数据库连接已关闭！");
            }
        }
    }
}
