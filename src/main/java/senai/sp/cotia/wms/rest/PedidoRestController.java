package senai.sp.cotia.wms.rest;

import java.io.FileInputStream;
import java.net.URI;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import senai.sp.cotia.wms.annotation.Privado;
import senai.sp.cotia.wms.annotation.Publico;
import senai.sp.cotia.wms.model.Aluno;
import senai.sp.cotia.wms.model.Enderecamento;
import senai.sp.cotia.wms.model.Estoque;
import senai.sp.cotia.wms.model.ItemNota;
import senai.sp.cotia.wms.model.ItemPedido;
import senai.sp.cotia.wms.model.Movimentacao;
import senai.sp.cotia.wms.model.NotaFiscal;
import senai.sp.cotia.wms.model.Pedido;
import senai.sp.cotia.wms.model.Produto;
import senai.sp.cotia.wms.repository.EnderecamentoRepository;
import senai.sp.cotia.wms.repository.EstoqueRepository;
import senai.sp.cotia.wms.repository.ItemNotaRepository;
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

	Long teste;

	// MÉTODO PARA SALVAR
	@RequestMapping(value = "save")
	public ResponseEntity<Pedido> savePedido(@RequestBody Pedido pedido, HttpServletRequest request,
			HttpServletResponse response) {

		// double total = pedido.totalPedido(pedido);

		Long cod = null;

		try {
			for (ItemPedido itens : pedido.getItens()) {
				itens.setPedido(pedido);
			}

			// pedido.setValor(total);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat parse = new SimpleDateFormat("dd-MM-yyyy");

			String data = parse.format(c.getTime());
			pedido.setDataPedido(data);
			pedidoRepo.save(pedido);
			saveMovimentacao(pedido);
			saveNotaFiscal(pedido);

			/*
			 * LocalDateTime time = LocalDateTime.now(); DateTimeFormatter fmt =
			 * DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"); NotaFiscal nota = new
			 * NotaFiscal(); nota.setDataEmissao(time.format(fmt)); //
			 * nota.setPedido(pedido); nota.setValorTotal(pedido.getValor()); //
			 * nota.setQuantidade(pedido.getTotalItens()); nfRepo.save(nota); for
			 * (ItemPedido itens : pedido.getItens()) { ItemNota item = new ItemNota();
			 * item.setNotaFiscal(nota); item.setPedido(pedido);
			 * item.setProduto(itens.getProduto()); //
			 * item.setQuantidade(itens.getQuantidade()); itemNotaRepository.save(item); }
			 */
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

	// MÉTODO PARA DELETAR PEDIDO
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirPedido(@PathVariable("id") Long numPedido) {
		pedidoRepo.deleteById(numPedido);
		return ResponseEntity.noContent().build();
	}

	// metodo para procurar um pedido à partir de qualquer atributo
	@RequestMapping(value = "/findbyall/{p}")
	public Iterable<Pedido> findByAll(@PathVariable("p") String param) {
		return pedidoRepo.procurarTudo(param);
	}

	@RequestMapping(value = "/saida/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> debitar(@PathVariable("id") Long idEndereco, @RequestBody Enderecamento endereco) {
		if (idEndereco != endereco.getId()) {
			throw new RuntimeException("ID inválido");
		}
		Movimentacao mov = new Movimentacao();
		mov.setTipo(Tipo.SAIDA);
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		mov.setData(time.format(fmt));
		mov.setProduto(endereco.getItens());
		movRepo.save(mov);

		if (endereco.getQuantidade() == 0) {
			endereco.setItens(null);
		}
		end.save(endereco);
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
				// itemNota.setItem();
				itemNota.setPedido(pedido);
				itemNotaRepository.save(itemNota);
			}

			return ResponseEntity.ok(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public Object saveMovimentacao(Pedido pedido) {
		for (ItemPedido itens : pedido.getItens()) {
			Movimentacao mov = new Movimentacao();
			mov.setProduto(itens.getProduto());
			mov.setTipo(Tipo.ENTRADA);
			LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			mov.setData(time.format(fmt));
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
			// nota.setQuantidade(pedido.getTotalItens());
			nfRepo.save(nota);
			for (ItemPedido itens : pedido.getItens()) {
				ItemNota item = new ItemNota();
				item.setNotaFiscal(nota);
				item.setPedido(pedido);
				item.setProduto(itens.getProduto());
				// item.setQuantidade(itens.getQuantidade());
				itemNotaRepository.save(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "teste/{id}")
	public ResponseEntity<ItemNota> teste(@PathVariable("id") Long nota) {
		List<ItemNota> list = itemNotaRepository.pegarNota(nota);
		System.out.println(list.size());
		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);
		Optional<NotaFiscal> notaToda = nfRepo.findById(nota);
		notaToda.get().getCodigoNota();

		
		try {

			/*LocalDateTime time = LocalDateTime.now();
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String dataFmt = fmt+"";*/
			JasperReport report = JasperCompileManager.compileReport("src/main/resources/notaFiscal.jrxml");
			Map<String, Object> map = new HashMap<>();
			String dataEmission = notaToda.get().getDataEmissao();
			String horaEntrada = dataEmission.substring(11);
			String dataFmt = dataEmission.substring(0, 11);
			map.put("codNota", notaToda.get().getCodigoNota());
			map.put("ItensPedido", bean);
			map.put("dataEmissao", dataFmt);
			map.put("horaEntrada", horaEntrada);

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, new JREmptyDataSource());

			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\TecDevTarde\\Downloads\\notaFiscal.pdf");

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();

	}

}
