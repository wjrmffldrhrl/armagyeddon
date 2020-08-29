package com.blockchain.armagyeddon.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.blockchain.armagyeddon.domain.entity.Gye;
import com.blockchain.armagyeddon.domain.entity.UserInfo;
import com.blockchain.armagyeddon.domain.repository.GyeRepository;
import com.blockchain.armagyeddon.domain.repository.UserInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

@Service
@Transactional
public class TokenService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private GyeRepository gyeRepository;

    // Token contract address

    private String armaTokenAddress = "0x5Ff9CBf9e40f015C2Ed728ac0967DC0Bb43fAb76";

    private String networkAddress = "http://ec2-52-14-89-25.us-east-2.compute.amazonaws.com:8545";

    private Web3j web3j;

    private Admin admin;
    List<String> addressList;

    // Connect blockchain server with web3j
    public TokenService() throws Exception {
        web3j = Web3j.build(new HttpService(networkAddress));
        admin = Admin.build(new HttpService(networkAddress));

        PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();

        addressList = personalListAccounts.getAccountIds();

    }

    // no transaction contract
    private List<Type> viewFunction(String functionName, List<Type> inputParameters,
                                    List<TypeReference<?>> outputParameters) throws IOException {

        Function function = new Function(functionName, inputParameters, outputParameters);

        System.out.println("function : " + function);

        // send transaction from address[0] to contract
        Transaction transaction = Transaction.createEthCallTransaction(addressList.get(0), armaTokenAddress,
                FunctionEncoder.encode(function));

        EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();


        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getResult(), function.getOutputParameters());

        return decode;
    }

    private void transactionFunction(String functionName, List<Type> inputParameters,

            List<TypeReference<?>> outputParameters) {


        // Create contract function
        Function function = new Function(functionName, inputParameters, outputParameters);

        // For the check nonce
        EthGetTransactionCount ethGetTransactionCount = null;

        // Get Transaction nonce
        try {

            ethGetTransactionCount = web3j.ethGetTransactionCount(addressList.get(0), DefaultBlockParameterName.LATEST)
                    .sendAsync().get();

        } catch (InterruptedException | ExecutionException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        // Create Transaction with nonce
        Transaction transaction = Transaction.createFunctionCallTransaction(addressList.get(0), nonce,
                Transaction.DEFAULT_GAS, null, armaTokenAddress, FunctionEncoder.encode(function));


        try {
            // Sent transaction
            web3j.ethSendTransaction(transaction).send();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // ledger에 쓰여지기 까지 기다리기.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // Get total supplied token
    public String totalSupply() throws Exception {

        List<Type> decode = viewFunction("totalSupply", Collections.emptyList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));

        return decode.get(0).getValue().toString();

    }

    // Get email user's balance
    public String getBalance(String email) throws Exception {

        String balance;
        UserInfo targetUser = userInfoRepository.findByEmail(email);

        if (targetUser == null)
            return "user [" + email + "] didn't exist";

        String address = targetUser.getPublicKey();

        System.out.println(address);

        List<Type> decode = viewFunction("balanceOf", Arrays.asList(new Address(address)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        balance = decode.get(0).getValue().toString();

        return balance;

    }

    // Get Gye Balance
    public String getBalance(Long gyeId) throws Exception {

        String balance;
        Gye targetGye = gyeRepository.findById(gyeId).get();

        if (targetGye == null)
            return "user [" + gyeId + "] didn't exist";

        String address = targetGye.getPublicKey();

        System.out.println(address);

        List<Type> decode = viewFunction("balanceOf", Arrays.asList(new Address(address)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        balance = decode.get(0).getValue().toString();

        return balance;

    }

    public boolean chargeToken(String email, String amount) {

        UserInfo targetUser = userInfoRepository.findByEmail(email);
        BigInteger amount_ = new BigInteger(amount);

        if (targetUser == null) {
            System.out.println("user [" + email + "] didn't exist");
            return false;
        }


        String address = targetUser.getPublicKey();

        System.out.println("user address : " + address);

        // Input contract argments
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(address));
        inputParameters.add(new Uint256(amount_));


        transactionFunction("mint", inputParameters, Collections.emptyList());

        return true;

    }



    // 토큰 전송 기능
    // 잔액이 부족한 경우에 대한 처리 추가할 것

    // to User
    public boolean sendTokenToUser(String from, String to, String amount) {
        UserInfo fromUser = userInfoRepository.findByEmail(from);
        UserInfo toUser = userInfoRepository.findByEmail(to);

        if (fromUser == null || toUser == null) {
            System.out.println("user didn't exist");
            return false;
        }

        sendToken(fromUser.getPublicKey(),  toUser.getPublicKey(), amount);


        return true;

    }

    // to Gye
    public boolean sendTokenToGye(String from, Long id, String amount) {

        UserInfo fromUser = userInfoRepository.findByEmail(from);
        Gye gye = gyeRepository.findById(id).get();

        if (fromUser == null || gye == null) {
            System.out.println("User or Gye didn't exist");
            return false;
        }

        sendToken(fromUser.getPublicKey(),  gye.getPublicKey(), amount);


        return true;

    }

    public boolean sendToken(String from, String to, String amount) {

        BigInteger amount_ = new BigInteger(amount);

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(from));
        inputParameters.add(new Address(to));
        inputParameters.add(new Uint256(amount_));


        transactionFunction("sendToken", inputParameters, Collections.emptyList());

        return true;
    }

    public boolean burnToken(String from, String amount) {
        UserInfo fromUser = userInfoRepository.findByEmail(from);


        BigInteger amount_ = new BigInteger(amount);

        if (fromUser == null) {
            System.out.println("user didn't exist");
            return false;
        }

        String fromUserAddress = fromUser.getPublicKey();

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(fromUserAddress));

        inputParameters.add(new Uint256(amount_));


        transactionFunction("burn", inputParameters, Collections.emptyList());

        return true;
    }


    public String createAccount(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchProviderException, CipherException {

        ECKeyPair keyPair = Keys.createEcKeyPair();

        WalletFile wallet = Wallet.createStandard(password, keyPair);       

        return "0x" + wallet.getAddress();
    }

    

}