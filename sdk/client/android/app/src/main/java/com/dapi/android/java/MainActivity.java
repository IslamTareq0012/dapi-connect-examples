package com.dapi.android.java;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dapi.android.R;
import com.dapi.connect.core.base.DapiAutoFlow;
import com.dapi.connect.core.base.DapiClient;
import com.dapi.connect.core.base.DapiConnect;
import com.dapi.connect.core.callbacks.OnDapiConnectListener;
import com.dapi.connect.core.callbacks.OnDapiTransferListener;
import com.dapi.connect.data.models.DapiBeneficiaryInfo;
import com.dapi.connect.data.models.DapiError;
import com.dapi.connect.data.models.LinesAddress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DapiClient dapiClient = DapiClient.Companion.getInstance();
        DapiConnect connect = dapiClient.getConnect();
        DapiAutoFlow autoFlow = dapiClient.getAutoFlow();
        //Starting Connect
        Button connectBtn = findViewById(R.id.connect);
        connectBtn.setOnClickListener(view -> {
            connect.present();
        });

        //Starting AutoFlow
        Button autoFlowBtn = findViewById(R.id.autoFlow);
        autoFlowBtn.setOnClickListener(view -> {
            //You may pass accountID and amount to navigate directly to the numpad
            autoFlow.present(null, 0);
        });

        //get cached connections
        connect.getConnections(dapiConnections -> {


            //HERE


            return null;
        }, dapiError -> {


            //In case there are errors, you get them here.


            return null;
        });

        //setting connect callbacks
        connect.setOnConnectListener(new OnDapiConnectListener(){

            //Called after successful connection.
            @Override
            public void onConnectionSuccessful(@NotNull String userID, @NotNull String bankID) {

            }

            //Called when the connection fails for any reason, you'll get info on the error in dapiError and logcat.
            @Override
            public void onConnectionFailure(@NotNull DapiError dapiError, @NotNull String bankID) {

            }

            //Called when the user taps continue (Shown after successful credentials input) on the webView then the webView disappears.
            //For example you may use this to show a loader while the connection is being processed in the SDK.
            @Override
            public void onProceed(@NotNull String userID, @NotNull String bankID) {

            }

            //You may use this to add a beneficiary to the newly connected account once the connection is done
            //Usually used when for example you're a seller and you want the newly connected account to add you as beneficiary Immediately
            //Note: Some banks has something called coolDownPeriod for adding a beneficiary that may take upto 2 days, so
            //by adding a beneficiary immediately here instead of adding the beneficiary when the buyer starts making a purchase,
            //you're actually winning some time.
            @Nullable
            @Override
            public DapiBeneficiaryInfo setBeneficiaryInfoOnConnect(@NotNull String bankID) {
                return null;
            }
        });

        //Setting autoflow callbacks
        autoFlow.setOnTransferListener(new OnDapiTransferListener() {
            //Called after a successful transfer
            @Override
            public void onAutoFlowSuccessful(double amount, @Nullable String senderAccountID, @Nullable String recipientAccountID, @NotNull String jobID) {

            }

            //Called when an error happens during making a transfer
            @Override
            public void onAutoFlowFailure(@NotNull DapiError dapiError, @Nullable String senderAccountID, @Nullable String recipientAccountID) {

            }

            //The beneficiary to send the money to.
            @NotNull
            @Override
            public DapiBeneficiaryInfo setBeneficiaryInfoOnAutoFlow(@NotNull String bankID) {
                LinesAddress linesAddress = new LinesAddress();
                linesAddress.setLine1("line1");
                linesAddress.setLine2("line2");
                linesAddress.setLine3("line3");
                String accountNumber = "xxxx";
                String name = "xxxx";
                String bankName = "xxxx";
                String swiftCode = "xxxx";
                String iban = "xxxx";
                String country = "xxxx";
                String branchAddress = "xxxx";
                String branchName = "xxxx";
                String phoneNumber = "xxxx";


                return new DapiBeneficiaryInfo(linesAddress, accountNumber, name, bankName, swiftCode, iban, country, branchAddress, branchName, phoneNumber);
            }

            @Override
            public void onPaymentStarted(@NotNull String bankID) {

            }


        });


        dapiClient.setUserID("USER_ID"); //This is Dapi's userID, you can get it using connect.getConnections() like above.
//        dapiClient.setConfigurations() //Used when you want to update DapiConfigurations at runtime



        //**************Calling APIs************************

        //get owner identity info
        dapiClient.getData().getIdentity(
                getIdentity-> {
                    return null;
                }, error -> {
                    return null;
                }
        );

        //Get accounts of the connection
        dapiClient.getData().getAccounts(
                getAccounts-> {
                    return null;
                }, error -> {
                    return null;
                }
        );

        //Get balance of some connected account
        dapiClient.getData().getBalance("accountID",
                getBalance-> {
                    return null;
                }, error -> {
                    return null;
                }
        );

        //Get transactions that took place between fromDate and toDate
        Date fromDate = new Date();
        Date toDate = new Date();
        dapiClient.getData().getTransactions( "accountID", fromDate, toDate,
                getTransactions-> {
                    return null;
                }, error -> {
                    return null;
                }
        );

        //Get metadata of the connected account
        dapiClient.getMetadata().getAccountMetaData(
                accountMetaData -> {
                    return null;
                },
                error -> {
                    return null;
                }
        );

        //Get beneficiaries of the connected account
        dapiClient.getPayment().getBeneficiaries(
                benefs -> {
                    return null;
                }, error -> {
                    return null;
                }
        );

//        Will NOT compile, add params to DapiBeneficiaryInfo
//        Add beneficiary to the connected account
//        dapiClient.getPayment().createBeneficiary( new DapiBeneficiaryInfo(),
//            createBenef -> {
//
//            }, error -> {
//
//                }
//        );

        //Send money from one account to another
        dapiClient.getPayment().createTransfer(
                "receiverID", "senderID", 1.0, "remark",
                createTransfer -> {
                    return null;
                }, error -> {
                    return null;
                }
        );

        //Send money from one account to another
        dapiClient.getPayment().createTransfer(
                "iban", "name", "senderID", 1.0, "remark",
                createTransfer -> {

                    return null;

                }, error -> {

                    return null;
                }
        );

        //Delink an account.
        dapiClient.getAuth().delink(
                delink-> {
                    return null;
                }, error ->{
                    return null;
                }
        );





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DapiClient dapiClient = DapiClient.Companion.getInstance();
        dapiClient.release();
    }
}