/**
 *  fabric-sdk-test
 */
package com.onechain.fabric.test;


import static java.lang.String.format;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.ChaincodeResponse.Status;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TestConfigHelper;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.testutils.TestConfig;
import org.hyperledger.fabric.sdkintegration.SampleOrg;
import org.hyperledger.fabric.sdkintegration.SampleStore;
import org.hyperledger.fabric.sdkintegration.SampleUser;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author fengxiang
 *
 */
public class TestMain {

	
	private static final String CHANNEL_NAME = "channel1";
    static HFClient hfclient = null;
    private static TestConfigHelper configHelper = new TestConfigHelper();
    private static Collection<SampleOrg> testSampleOrgs;
    private static final TestConfig testConfig = TestConfig.getConfig();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 //peerTest();
		//peerTest();
		testnew();
	
	}
	
	
	
	public static void testPeer(){
		
		System.out.println(System.getProperty("user.home"));
		
	}
	
	
	public static  void testclient(){
		
		
	}
	
	public static void testnew(){
		
		
		
		
        try {
        	
        	HFClient hfclient = HFClient.createNewInstance();
        	
        	CryptoSuite cryptosuite= CryptoSuite.Factory.getCryptoSuite();
        	
        	cryptosuite.init();
        	//cryptosuite.
        	
			hfclient.setCryptoSuite(cryptosuite);
			HFCAClient caclient = HFCAClient.createNewInstance( "http://localhost:7054",null  );
			
						
			
			caclient.setCryptoSuite(cryptosuite);
			Enrollment enrollment = caclient.enroll(  "admin"  ,  "adminpw"  );
			
			  
			File sampleStoreFile = new File(System.getProperty("user.home") + "/HFCSampletest.properties");
			if (sampleStoreFile.exists()) { // For testing start fresh
			    sampleStoreFile.delete();
			}
			SampleStore sampleStore = new SampleStore(sampleStoreFile);
			SampleUser admin = sampleStore.getMember("admin","org1");
			admin.setMspId("Org1MSP");
			
			admin.setEnrollment(enrollment);
			
			hfclient.setUserContext(admin);
			
			testchannel = hfclient.newChannel("mychannel");
						
			Peer peer = hfclient.newPeer( "peertest", "grpc://localhost:7051");
			Orderer order = hfclient.newOrderer("order", "grpc://localhost:7050");
			testchannel.addPeer( peer );
			testchannel.addOrderer(order);

			testchannel.initialize();

			
			BlockInfo blockinfo= testchannel.queryBlockByNumber(2);
			
			System.out.println(blockinfo.getBlock().toString());
			
			
		} catch (CryptoException | InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EnrollmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProposalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		
	}
	
	
	public static HFClient newInstance() throws Exception {


        File tempFile = File.createTempFile("teststore", "properties");
        tempFile.deleteOnExit();

        File sampleStoreFile = new File(System.getProperty("user.home") + "/test.properties");
        
        if (sampleStoreFile.exists()) { //For testing start fresh
            sampleStoreFile.delete();
        }
        
        final SampleStore sampleStore = new SampleStore(sampleStoreFile);

        //src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/

        //SampleUser someTestUSER = sampleStore.getMember("someTestUSER", "someTestORG");
        SampleUser someTestUSER = sampleStore.getMember("someTestUSER", "someTestORG", "mspid",
                findFileSk("src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore"),
                new File("src/test/fixture/sdkintegration/e2e-2Orgs/channel/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem"));
        someTestUSER.setMspId("testMSPID?");

        HFClient hfclient = HFClient.createNewInstance();
        hfclient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());


        hfclient.setUserContext(someTestUSER);

        return hfclient;

    }

	
	static File findFileSk(String directorys) {

        File directory = new File(directorys);

        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));

        if (null == matches) {
            throw new RuntimeException(format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }

        if (matches.length != 1) {
            throw new RuntimeException(format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }

        return matches[0];

    }
	
	
	
	/////////////////////////    ======================== PEER  TEST =======================   ///////////////////////////
	
	static Channel testchannel = null;
    static ChaincodeResponse deployResponse = null;
	
    
	public static void peerTest(){
		
		try {
			
			testSampleOrgs = testConfig.getIntegrationTestsSampleOrgs();
			
			configHelper.clearConfig();
	        configHelper.customizeConfig();

	        testSampleOrgs = testConfig.getIntegrationTestsSampleOrgs();
	        //Set up hfca for each sample org

	      
	        HFClient hfclient1 = newInstance();
	        
	        
	        for (SampleOrg sampleOrg : testSampleOrgs) {
	        	
	            String caName = sampleOrg.getCAName(); //Try one of each name and no name.
	            if (caName != null && !caName.isEmpty()) {
	                sampleOrg.setCAClient(HFCAClient.createNewInstance(caName, sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
	            } else {
	                sampleOrg.setCAClient(HFCAClient.createNewInstance(sampleOrg.getCALocation(), sampleOrg.getCAProperties()));
	            }
	            
	        }
			
			
	        //hfclient1.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

	        
			Peer peer = hfclient1.newPeer( "peertest", "grpc://localhost:7051");
			
			System.out.println(peer.getName());
			
			//testChain = new Channel( "chain1" ,null );
			//testChain = new Channel
			
			//testChain = new Channel( null , null );
			//testchannel = hfclient1.getChannel("mychannel");
			
			testchannel = hfclient1.newChannel("mychannel");
			testchannel.initialize();
			
			testchannel.addPeer( peer );
			
			Properties ordererProperties = new Properties();

			//example of setting keepAlive to avoid timeouts on inactive http2 connections.
			// Under 5 minutes would require changes to server side to accept faster ping rates.
			ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[] {5L, TimeUnit.MINUTES});
			ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[] {8L, TimeUnit.SECONDS});


			Orderer order = hfclient1.newOrderer("order", "grpc://localhost:7050");

			
			testchannel.addOrderer(order);
			
			
			BlockInfo blockinfo= testchannel.queryBlockByNumber(2);
		
			
          
			String moveAmount = "10";
			
			User user = null;
			
			//testchannel.queryByChaincode(queryByChaincodeRequest)
//			try {
//	            Collection<ProposalResponse> successful = new LinkedList<>();
//	            Collection<ProposalResponse> failed = new LinkedList<>();
//
//	            
//	            ChaincodeID chaincodeID = ChaincodeID.newBuilder().setName("dddd")
//	                    .setVersion("11")
//	                    .setPath("dddddd").build();
//	            
//	            ///////////////
//	            /// Send transaction proposal to all peers
//	            TransactionProposalRequest transactionProposalRequest = hfclient1.newTransactionProposalRequest();
//	            transactionProposalRequest.setChaincodeID(chaincodeID);
//	            transactionProposalRequest.setFcn("invoke");
//	            transactionProposalRequest.setArgs(new String[] {"move", "a", "b", "11"});
//	            transactionProposalRequest.setProposalWaitTime(5000);
//	            if (user != null) { // specific user use that
//	                transactionProposalRequest.setUserContext(user);
//	            }
//	            out("sending transaction proposal to all peers with arguments: move(a,b,%s)", moveAmount);
//
//	            Collection<ProposalResponse> invokePropResp = testchannel.sendTransactionProposal(transactionProposalRequest);
//	            for (ProposalResponse response : invokePropResp) {
//	                if (response.getStatus() == Status.SUCCESS) {
//	                	out("Successful transaction proposal response Txid: %s from peer %s", response.getTransactionID(), response.getPeer().getName());
//	                    successful.add(response);
//	                } else {
//	                    failed.add(response);
//	                }
//	            }
//
//	            out("Received %d transaction proposal responses. Successful+verified: %d . Failed: %d",
//	                    invokePropResp.size(), successful.size(), failed.size());
//	            if (failed.size() > 0) {
//	                ProposalResponse firstTransactionProposalResponse = failed.iterator().next();
//
//	                throw new ProposalException(format("Not enough endorsers for invoke(move a,b,%s):%d endorser error:%s. Was verified:%b",
//	                        moveAmount, firstTransactionProposalResponse.getStatus().getStatus(), firstTransactionProposalResponse.getMessage(), firstTransactionProposalResponse.isVerified()));
//
//	            }
//	            System.out.println("Successfully received transaction proposal responses.");
//
//	            ////////////////////////////
//	            // Send transaction to orderer
//	            out("Sending chaincode transaction(move a,b,%s) to orderer.", moveAmount);
//	            if (user != null) {
//	                 testchannel.sendTransaction(successful, user);
//	            }
//	             testchannel.sendTransaction(successful);
//	        } catch (Exception e) {
//
//	            e.printStackTrace();
//
//	        }

			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
	}
	
	
	static void out(String format, Object... args) {

        System.err.flush();
        System.out.flush();

        System.out.println(format(format, args));
        System.err.flush();
        System.out.flush();

    }
	

}


