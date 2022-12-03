  package senai.sp.cotia.wms.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.io.Files;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.repository.EnderecamentoRepository;
import senai.sp.cotia.wms.repository.ItemNotaRepository;
import senai.sp.cotia.wms.repository.ItemPedidoRepository;
import senai.sp.cotia.wms.repository.MovimentacaoRepository;
import senai.sp.cotia.wms.repository.NotaFiscalRepository;
import senai.sp.cotia.wms.repository.PedidoRepository;
import senai.sp.cotia.wms.repository.ProdutoRepository;
import senai.sp.cotia.wms.type.Tipo;

@RestController
@CrossOrigin
@RequestMapping("api/pedido")
public class PedidoRestController {

	@Autowired
	private PedidoRepository pedidoRepo;

	@Autowired
	private ItemNotaRepository itemNotaRepository;

	@Autowired
	private NotaFiscalRepository nfRepo;

	@Autowired
	private MovimentacaoRepository movRepo;

	@Autowired
	private EnderecamentoRepository end;

	@Autowired
	private ProdutoRepository produtoRp;

	@Autowired
	private ItemPedidoRepository itemPedidoRep;
	
	// MÉTODO PARA SALVAR
	@RequestMapping(value = "save")
	public ResponseEntity<Pedido> savePedido(@RequestBody Pedido pedido, HttpServletRequest request,
			HttpServletResponse response) {

		int totalProdutos = 0;

		try {

			for (ItemPedido itens : pedido.getItens()) {
				itens.setPedido(pedido);
				totalProdutos +=itens.getQuantidade();
			} 


			
			pedido.setTotalItens(totalProdutos);

			Calendar c = Calendar.getInstance();
			SimpleDateFormat parse = new SimpleDateFormat("dd-MM-yyyy");
			
			String data = parse.format(c.getTime());
			pedido.setDataPedido(data);
			
			pedidoRepo.save(pedido);
			saveMovimentacao(pedido);
			saveNotaFiscal(pedido);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Iterable<Pedido> listarPedidos() {
		return pedidoRepo.findAll();
	}

	// MÉTODO PARA BUSCAR PEDIDO NO BANCO
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> findPedido(@PathVariable("id") Long numPedido) {
		// buscar pedido
		Optional<Pedido> pedido = pedidoRepo.findById(numPedido);

		// verificação de pedido
		if (pedido.isPresent()) {
			return ResponseEntity.ok(pedido.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// MÉTODO PARA SALVAR ATUALIZAÇÃO
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> atualizarPedido(@RequestBody Pedido pedido, @PathVariable("id") Long idPedido) {
		// validação de id
		if (idPedido != pedido.getNumPedido()) {
			throw new RuntimeException("ID inválido");
		}
		// salvar pedido atualizado
		pedidoRepo.save(pedido);
		// criar novo cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/pedido"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}
	
	// MÉTODO PARA SALVAR ATUALIZAÇÃO
	@RequestMapping(value = "enderecado/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> endecado(@RequestBody Pedido pedido, @PathVariable("id") Long idPedido) {
		
		Pedido pedidoBd = pedidoRepo.findBynumPedido(idPedido);
		pedidoBd.setEnderecado(pedido.isEnderecado());
		// salvar pedido atualizado
		System.out.println(pedido);
		pedidoRepo.save(pedidoBd);
		// criar novo cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/pedido"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	// MÉTODO PARA DELETAR PEDIDO
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirPedido(@PathVariable("id") Long numPedido) {
		pedidoRepo.deleteById(numPedido);
		return ResponseEntity.noContent().build();
	}

	// metodo para procurar um pedido à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}", method = RequestMethod.GET)
	public Iterable<Pedido> findByAll(@PathVariable("p") String param) {
		return pedidoRepo.procurarTudo(param);
	}

	@RequestMapping(value = "/saida/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> debitar(@PathVariable("id") Long idEndereco, @RequestBody Enderecamento endereco) {

		if (idEndereco != endereco.getId()) {
			throw new RuntimeException("ID inválido");
		}
		
		Optional<Enderecamento> enderecoBd = end.findById(idEndereco);
		Movimentacao mov = new Movimentacao();
		mov.setTipo(Tipo.SAIDA);
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		//mov.setData();
		mov.setProduto(endereco.getItens());
		mov.setQuantidade(enderecoBd.get().getQuantidade() - endereco.getQuantidade());
		movRepo.save(mov);

		if (endereco.getQuantidade() <= 0) {
			end.delete(endereco);
		}else {
			end.save(endereco);
		}
		
		// criar novo cabeçalho HTTP
		HttpHeaders header = new HttpHeaders();
		header.setLocation(URI.create("/api/pedido"));
		return new ResponseEntity<Void>(header, HttpStatus.OK);
	}

	public ResponseEntity<Object> saveItensNota(Pedido pedido, NotaFiscal nota) {
		try {
			for (ItemNota itens : nota.getItens()) {
				ItemNota itemNota = new ItemNota();
				itemNota.setNotaFiscal(nota);
				itemNota.setPedido(pedido);
				itemNotaRepository.save(itemNota);
			}

			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public Object saveMovimentacao(Pedido pedido) throws ParseException {

		for (ItemPedido itens : pedido.getItens()) {
			Movimentacao mov = new Movimentacao();
			mov.setProduto(itens.getProduto());
			mov.setTipo(Tipo.ENTRADA);
			SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
			
			Date data =new Date();
			
		
			mov.setData(data);
			mov.setQuantidade(itens.getQuantidade());
			movRepo.save(mov);
		

		}

		return "deu certo";
	}

	public ResponseEntity<Object> saveNotaFiscal(Pedido pedido) {

		try {
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			NotaFiscal nota = new NotaFiscal();
			nota.setDataEmissao(time.format(fmt));
			// nota.setPedido(pedido);
			nota.setValorTotal(pedido.getValor());
			nota.setPedido(pedido);

			// nota.setQuantidade(pedido.getTotalItens());
			nota.setPedido(pedido);
			nfRepo.save(nota);

			for (ItemPedido itens : pedido.getItens()) {
				ItemNota item = new ItemNota();
				item.setNotaFiscal(nota);
				item.setPedido(pedido);
				item.setProduto(itens.getProduto());
				item.setQuantidade(itens.getQuantidade());
				itemNotaRepository.save(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "teste/{id}")
	public ResponseEntity<ItemNota> teste(@PathVariable("id") Long nota,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		List<ItemNota> list = itemNotaRepository.pegarNota(nota);

		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);
		Optional<NotaFiscal> notaToda = nfRepo.findById(nota);
		notaToda.get().getCodigoNota();
		response.setContentType("apllication/pdf");

		response.addHeader("Content-Disposition", "inline; filename=" + "codigo.pdf");
		
		try {


			/*
			  LocalDateTime time = LocalDateTime.now(); DateTimeFormatter fmt =
			  DateTimeFormatter.ofPattern("dd-MM-yyyy"); String dataFmt = fmt+"";
			 */
			JasperReport report = JasperCompileManager
					.compileReport(getClass().getResourceAsStream("/relatorios/notaFiscal.jrxml"));

			/*LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String dataFmt = fmt+"";*/
			Map<String, Object> map = new HashMap<>();
			String dataEmission = notaToda.get().getDataEmissao();
			String horaEntrada = dataEmission.substring(11);
			String dataFmt = dataEmission.substring(0, 11);
			map.put("codNota", notaToda.get().getCodigoNota());
			map.put("dataEmissao", dataFmt);
			map.put("horaEntrada", horaEntrada);

			String name = "notaFiscal.pdf";
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, bean);
			JasperExportManager.exportReportToPdfFile(jasperPrint, name);
			
			File arquivo = new File(name);

			OutputStream output = response.getOutputStream();
			Files.copy(arquivo, output);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();

	}
	@RequestMapping(value = "/findbypedido/{codigo}")
    public List<ItemPedido> findAllByPedido(@PathVariable("codigo") Long param) {
        return itemPedidoRep.pegarItens(param);
    }
	
	@GetMapping(value = "pedidosAluno/{id}")
	public List<Pedido>pegaPedidoDoAluno(@PathVariable("id")Long param){
		return pedidoRepo.pegarPedidosAluno(param);
	}
	@GetMapping(value = "pedidosProfessor/{id}")
	public List<Pedido>pegaPedidoDoProf(@PathVariable("id")Long param){
		return pedidoRepo.pegarPedidosProf(param);
	}
	

}
