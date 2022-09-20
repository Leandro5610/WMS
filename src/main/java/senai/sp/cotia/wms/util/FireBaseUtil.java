package senai.sp.cotia.wms.util;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import senai.sp.cotia.wms.model.Aluno;

@Service
public class FireBaseUtil {
	// variavel para guardar as credencias do firebase
	private Credentials credential;
	
	//variavel para acessar o storage
	private Storage storage;
	
	//constate para bucket name
	private final String BUCKET_NAME = "systemwms-14aa0.appspot.com";
	
	// constate para o prefixo da URL
	private final String PREFFIX ="https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	
	//constante para sufixo da URL
	private final String SUFFIX = "?alt=media";
	
	//constanta para URL 
	private final String DOWNLOAD_URL = PREFFIX +"%s" + SUFFIX;
	
	
	public FireBaseUtil() {
		// usar as credenciais da chave do fire base
		Resource resource = new ClassPathResource("chavefirebase.json");
		
		try {
			//ler o arquivo
			credential = GoogleCredentials.fromStream(resource.getInputStream());
			
			//acessao oderviço de storage
			storage = StorageOptions.newBuilder().setCredentials(credential).build().getService();
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e.getMessage());

		}
	}
	
	public String uploadFile(File arquivo, byte[] arq) throws IOException {
		
		//gera uma string aleatoria para o nome do arquivo
		//String nomeArquivo = UUID.randomUUID().toString()+getExtensao(arquivo);
		//criar um BlobId
		BlobId blobId = BlobId.of(BUCKET_NAME, arquivo.toString());

		
		//blobinfo a partir do bloid
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		//manda o info para o storage passando os bytes do arquivo para ele
		storage.create(blobInfo, arq);
		//retornar a url para acessar o arquivo
		return String.format(DOWNLOAD_URL,arquivo);
	}
	//retorna a extensao do arquivo atraves do nome
		private String getExtensao(String nomeArquivo) {
			return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
		}
		
		public void removeFile(String nomeArquivo) {
			//retira o prefixo e sufixo do arquivo
			nomeArquivo = nomeArquivo.replace(PREFFIX, "").replace(SUFFIX, "");
			// pego o blob atraves do nome do arquivo
			Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
			//deleta o arquivo
			storage.delete(blob.getBlobId());
		}
}
