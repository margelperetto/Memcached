package org.teste.memcached;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.teste.memcached.entidades.ContatoTeste;
import org.teste.memcached.entidades.EntidadeTeste;
import org.teste.memcached.entidades.NonDefaultConstructorEntity;
import org.teste.memcached.entidades.OverheadEntity;
import org.teste.memcached.entidades.SEBlobImpl;
import org.teste.memcached.entidades.SimpleEntity;

import org.teste.memcached.entidades.ContatoTeste.Tipo;
import org.teste.memcached.utils.SerializeUtils;

public class SerializeUtilsTest {

	@Test
	public void testSimpleObj(){

		SimpleEntity simple = createSimpleEntity();
		String json = SerializeUtils.toJSON(simple);
		SimpleEntity ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE SIMPLES: "+ret);
	}

	@Test
	public void testListSimpleObj(){

		List<SimpleEntity> list = new ArrayList<>();
		list.add(createSimpleEntity(1));
		list.add(createSimpleEntity(2));

		String json = SerializeUtils.toJSON(list);
		List<SimpleEntity> ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE LISTA SIMPLES: "+ret);
	}
	
	@Test
	public void testMapSimpleObj(){
		
		Map<String, SimpleEntity> map = new HashMap<>();
		map.put("Teste1", createSimpleEntity(1));
		map.put("Teste2", createSimpleEntity(2));
		
		String json = SerializeUtils.toJSON(map);
		Map<String, SimpleEntity> ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE MAP SIMPLES: "+ret);
	}
	
	@Test
	public void testMapEmpty(){
		
		Map<String, SimpleEntity> map = new HashMap<>();
		
		String json = SerializeUtils.toJSON(map);
		Map<String, SimpleEntity> ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE MAP EMPTY: "+ret);
	}

	@Test
	public void testEntidadeComplexa(){

		EntidadeTeste obj = buildEntidadeTeste(0);
		String json = SerializeUtils.toJSON(obj);
		EntidadeTeste ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE COMPLEXA: "+ret);
	}

	@Test
	public void testEntidadeOverride(){

		OverheadEntity obj = new OverheadEntity();
		obj.setData("TESTE".getBytes());

		String json = SerializeUtils.toJSON(obj);
		OverheadEntity ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE OVERRIDE: "+ret);
	}

	@Test
	public void testNonDefaultConstructor(){

		NonDefaultConstructorEntity obj = new NonDefaultConstructorEntity("TESTE");

		String json = SerializeUtils.toJSON(obj);
		NonDefaultConstructorEntity ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE NON DEFAULT CONSTRUCTOR: "+ret);
	}

	@Test
	public void testBlobImlp(){

		SEBlobImpl obj = new SEBlobImpl("TESTE".getBytes());

		String json = SerializeUtils.toJSON(obj);
		SEBlobImpl ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE BLOBIMPL: "+ret);
	}

	@Test
	public void testString(){

		String obj = "TESTE COM STRING";
		String json = SerializeUtils.toJSON(obj);
		String ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE String: "+ret);
	}

	@Test
	public void testDouble(){

		Double obj = 15.5;
		String json = SerializeUtils.toJSON(obj);
		Double ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE Double: "+ret);
	}
	
	@Test
	public void testPrimitiveBoolean(){
		
		boolean obj = false;
		String json = SerializeUtils.toJSON(obj);
		boolean ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE boolean: "+ret);
	}
	
	@Test
	public void testBoolean(){
		
		Boolean obj = false;
		String json = SerializeUtils.toJSON(obj);
		Boolean ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE Boolean: "+ret);
	}
	
	@Test
	public void testInt(){
		
		int obj = 13;
		String json = SerializeUtils.toJSON(obj);
		int ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE boolean: "+ret);
	}
	
	@Test
	public void testInteger(){
		
		Integer obj = 45;
		String json = SerializeUtils.toJSON(obj);
		Integer ret = SerializeUtils.fromJSON(json);
		
		System.out.println("TESTE Integer: "+ret);
	}

	@Test
	public void testLoreIpsum(){

		SimpleEntity simple = createSimpleEntity();
		simple.setNome(getLoremIpsum());
		String json = SerializeUtils.toJSON(simple);
		SimpleEntity ret = SerializeUtils.fromJSON(json);

		System.out.println("TESTE LOREM IPSUM: "+ret);
	}

	private EntidadeTeste buildEntidadeTeste(int i) {
		List<ContatoTeste> contatos = new ArrayList<>();
		contatos.add(new ContatoTeste("fone "+i, Tipo.TELEFONE));
		contatos.add(new ContatoTeste("mail "+i, Tipo.EMAIL));

		EntidadeTeste ent = new EntidadeTeste("Nome "+i, new BigDecimal(i*10), new Date());
		ent.setContatos(contatos);

		return ent;
	}

	private SimpleEntity createSimpleEntity(Integer index) {
		SimpleEntity simple = new SimpleEntity();
		simple.setIdade(10);
		simple.setNome("Teste "+(index==null?"":index));
		return simple;
	}

	private SimpleEntity createSimpleEntity() {
		return createSimpleEntity(null);
	}

	private String getLoremIpsum() {
		return 
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
				+"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"
				+"But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure?"
				+"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."
				+"On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains."
				;
	}

}
