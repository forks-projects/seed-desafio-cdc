# Desafio Implementando uma loja de livros virtual

## Desafio 01: Implementando uma loja de livros virtual
### Setup do projeto
​Crie um novo projeto utilizando as tecnologias que você decidiu trabalhar. Lembre de escolher algo que você sinta que vai te ajudar na sua carreira! Abaixo algumas combinações que são famosas no mercado:

- Java + Spring via Spring boot
- Kotlin + Spring via Spring boot
- Typescript + Nestjs
- C# + asp.net core

### Como aproveitar o desafio da casa do código
Neste desafio você precisa desenvolver uma api para suportar parte do funcionamento da casa do código. Temos várias funcionalidades de dificuldade progressiva e com uma regra de negócio que eu imagino que seja de um entendimento mais direto. 

Especificamente aqui, eu sugiro que você aproveite para consumir o máximo das sugestões de código que eu vou te dar. A ideia é que você trabalhe o design do seu código, criando código que seja suficiente para a funcionalidade e que comece a questionar de maneira propositiva os padrões já estabelecidos na sua mente e no mercado.

Deixando claro, ter padrões é muito bom. Eles servem para você bater o olho numa estrutura e já entender múltiplas coisas de uma vez só. Só que questioná-los também é muito saudável, já que nada está escrito em pedra. Durante a jornada eu apresento para você novos padrões e você precisa ficar de mente aberta. A ideia é apenas te dar mais ferramentas. 

### Faça um fork para começar o projeto
Faça um fork desse [REPOSITÓRIO](https://github.com/asouza/seed-desafio-cdc) e implemente seu código nesse fork :).

### Cadastro de um novo categoria

É necessário cadastrar um novo categoria no sistema. Todo categoria tem um nome, email e uma descrição. Também queremos saber o instante exato que ele foi registrado.

**Restrições**

- O instante não pode ser nulo
- O email é obrigatório
- O email tem que ter formato válido
- O nome é obrigatório
- A descrição é obrigatória e não pode passar de 400 caracteres 

**Resultado esperado**
- Um novo categoria criado e status 200 retornado

**informações de suporte geral**

Será que você fez um código parecido com esse exemplo AQUI ?

Se a resposta para o ponto 1 foi sim, recomendo de novo esse material aqui sobre ARQUITETURA X DESIGN. Também acho que vai ser legal você olhar a MINHA IMPLEMENTAÇÃO LOGO DE CARA, apenas para ter uma ideia de design que estou propondo.

CONTROLLERS 100% COESOS para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.

Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? DÁ UMA OLHADA NESSE PILAR AQUI.

Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? SUGIRO OLHAR UM POUCO SOBRE NOSSA IDEIA DE FORM VALUE OBJECTS.

Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele AQUI e depois AQUI
TODO FRAMEWORK MVC MINIMAMENTE MADURO POSSUI UM MECANISMO PRONTO DE REALIZAR VALIDAÇÃO CUSTOMIZADA. SPRING, NESTJS E ASP.NET CORE MVC TÊM.

Nome,email e descrição são informações obrigatórias. Como você lidou com isso? INFORMAÇÃO NATURAL E OBRIGATÓRIA ENTRA PELO CONSTRUTOR

Deixamos pistas que facilitem o uso do código onde não conseguimos resolver com compilação. Muitas vezes recebemos String, ints que possuem significados. Um email por exemplo. Se você não pode garantir a validação do formato em compilação, QUE TAL DEIXAR UMA DICA PARA A OUTRA PESSOA?

Utilize um insomnia ou qualquer outra forma para verificar o endpoint
PEGUE CADA UMA DAS CLASSES QUE VOCÊ CRIOU E REALIZE A CONTAGEM DA CARGA INTRÍNSECA. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.

COMO ALBERTO FARIA ESSE CÓDIGO?

**informações de suporte para a combinação Java/Kotlin + Spring**

- Para receber os dados da request como json, temos a annotation @RequestBody
- Usamos a annotation @Valid para pedir que os dados da request sejam validados
- Para realizar as validações padrões existe a Bean Validation
- COMO CRIAR UM @RESTCONTROLLERADVICE PARA CUSTOMIZAR O JSON DE SAÍDA COM ERROS DE VALIDAÇÃO
- COMO EXTERNALIZAR AS MENSAGENS DE ERRO NO ARQUIVO DE CONFIGURAÇÃO.


### Email do categoria é único
**necessidades**    
O email do categoria precisa ser único no sistema

**resultado esperado**    
Erro de validação no caso de email duplicado

**informações de suporte geral**    
- TODO FRAMEWORK MVC MINIMAMENTE MADURO POSSUI UM MECANISMO PRONTO DE REALIZAR VALIDAÇÃO CUSTOMIZADA. SPRING, NESTJS E ASP.NET CORE MVC TÊM.
- Aqui provavelmente você terá um if em algum lugar para verificar a existência de um outro categoria. Todo código que tem uma branch de código(if,else) tem mais chance de executar de maneira equivocada. Tente criar um teste automatizado para aumentar ainda mais a confiabilidade do seu código. CRIAMOS TESTES AUTOMATIZADOS PARA QUE ELE NOS AJUDE A REVELAR E CONSERTAR BUGS NA APLICAÇÃO.​​ 
- COMO ALBERTO FARIA ESSE CÓDIGO?

**informações de suporte para a combinação Java/Kotlin + Spring**    
- Para receber os dados da request como json, temos a annotation @RequestBody
- Usamos a annotation @Valid para pedir que os dados da request sejam validados
- Para realizar as validações padrões existe a Bean Validation
- COMO CRIAR UM @RESTCONTROLLERADVICE PARA CUSTOMIZAR O JSON DE SAÍDA COM ERROS DE VALIDAÇÃO
- COMO EXTERNALIZAR AS MENSAGENS DE ERRO NO ARQUIVO DE CONFIGURAÇÃO.


### Cadastro de uma categoria
**necessidades**:    
- Toda categoria precisa de um nome

**restrições**:    
- O nome é obrigatório
- O nome não pode ser duplicado

**resultado esperado**:    
- Uma nova categoria cadastrada no sistema e status 200 retorno
- Caso alguma restrição não seja atendida, retorne 400 e um json informando os problemas de validação

**sobre a utilização do material de suporte aqui**:    
Esta é uma feature bem parecida com a de cadastro de categoria. Tente implementar inicialmente sem utilizar nenhum material de suporte. Caso sinta dificuldade vá utilizando de acordo com a necessidade. ​

**informações de suporte geral**:    
- CONTROLLERS 100% COESOS para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.
- Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? DÁ UMA OLHADA NESSE PILAR AQUI.
- Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? SUGIRO OLHAR UM POUCO SOBRE NOSSA IDEIA DE FORM VALUE OBJECTS.
- Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele AQUI e depois AQUI
- Nome é obrigatório. Como você lidou com isso? INFORMAÇÃO NATURAL E OBRIGATÓRIA ENTRA PELO CONSTRUTOR
- Deixamos pistas que facilitem o uso do código onde não conseguimos resolver com compilação. Muitas vezes recebemos String, ints que possuem significados. O nome aqui é obrigatório, mas você não consegue garantir isso em tempo de compilação(caso esteja utilizando uma linguagem compilada). Se você não pode garantir a validação do formato em compilação, QUE TAL DEIXAR UMA DICA PARA A OUTRA PESSOA?
- TODO FRAMEWORK MVC MINIMAMENTE MADURO POSSUI UM MECANISMO PRONTO DE REALIZAR VALIDAÇÃO CUSTOMIZADA. SPRING, NESTJS E ASP.NET CORE MVC TÊM.
- Utilize um insomnia ou qualquer outra forma para verificar o endpoint
- PEGUE CADA UMA DAS CLASSES QUE VOCÊ CRIOU E REALIZE A CONTAGEM DA CARGA INTRÍNSECA. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.
- COMO ALBERTO FARIA ESSE CÓDIGO?

### Criação de um validador customizado genérico para verificar unicidade de determinada informação
Tanto para o cadastro do autor quanto para o cadastro da categoria, foi necessário realizar uma validação de valor único no sistema. Neste caso, só muda um detalhe da query que estamos executando para fazer a verificação. E agora, será que você consegue criar seu validador customizado para reutilizá-lo nas validações de email de autor e nome de categoria? 

**informações de suporte geral**    
O desafio aqui é conhecer mais profundamente sobre o mecanismo de validação que está sendo utilizado no seu projeto. 
COMO SERÁ QUE ALBERTO FEZ?

[Exemplo  de validador único](
https://github.com/adrianoavelinozup/orange-talents-09-template-casa-do-codigo/blob/main/src/main/java/br/com/zupacademy/adriano/casadocodigo/annotation/ValidadorUnicoValidator.java)


### Criar um novo livro

**necessidades**    
- Um título
- Um resumo do que vai ser encontrado no livro
- Um sumário de tamanho livre. O texto deve entrar no formato markdown, que é uma string. Dessa forma ele pode ser formatado depois da maneira apropriada.
- Preço do livro
- Número de páginas
- Isbn(identificador do livro)
- Data que ele deve entrar no ar(de publicação)
- Um livro pertence a uma categoria
- Um livro é de um autor

**restrições**    
- Título é obrigatório
- Título é único
- Resumo é obrigatório e tem no máximo 500 caracteres
- Sumário é de tamanho livre.
- Preço é obrigatório e o mínimo é de 20
- Número de páginas é obrigatória e o mínimo é de 100
- Isbn é obrigatório, formato livre
- Isbn é único
- Data que vai entrar no ar precisa ser no futuro
- A categoria não pode ser nula
- O autor não pode ser nulo


**resultado esperado**    
- Um novo livro precisa ser criado e status 200 retornado
- Caso alguma restrição não seja atendida, retorne 400 e um json informando os problemas de validação


**sobre a utilização do material de suporte aqui**    
Esta é uma feature também bem parecida com o cadastro de categoria e autor. Por mais que ela tenha bem mais campos, os conhecimentos necessários para a implementação são os mesmos. Tente muito fazer sem olhar nenhum material de suporte. Se estiver complicado, tenta mais um pouco. É neste momento de busca da informação e organização das informações que já temos que o conhecimento vai se consolidando. 

Caso sinta que precisa de suporte, utilize o material de suporte de maneira bem progressiva. Lembre que também temos nosso canal do discord e você pode pedir uma ajudinha por lá :). 

**informações de suporte para a feature**    
CONTROLLERS 100% COESOS para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.
Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? DÁ UMA OLHADA NESSE PILAR AQUI.
Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? SUGIRO OLHAR UM POUCO SOBRE NOSSA IDEIA DE FORM VALUE OBJECTS. Neste caso aqui usar a ideia do Form Value Object é ainda mais interessante. Um livro precisa de autor, categoria etc. O código de transformação tem um esforço de entendimento ainda maior.
Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele AQUI e depois AQUI
O livro tem muitas informações obrigatórias. Aqui a palavra chave é obrigatoriedade. Como você lidou com isso? INFORMAÇÃO NATURAL E OBRIGATÓRIA ENTRA PELO CONSTRUTOR
Um construtor com muitos argumentos de tipo parecido pode gerar dificuldade para uma pessoa acertar a ordem dos parâmetros. Que tal você olhar para um pattern escrito no livro Design Patterns chamado Builder?
Deixamos pistas que facilitem o uso do código onde não conseguimos resolver com compilação. Muitas vezes recebemos String, ints que possuem significados. Um email por exemplo. Se você não pode garantir a validação do formato em compilação, QUE TAL DEIXAR UMA DICA PARA A OUTRA PESSOA? Lembre que se tiver optado pelo construtor, a pista fica ainda mais importante dado o número de argumentos que são necessários.
TODO FRAMEWORK MVC MINIMAMENTE MADURO POSSUI UM MECANISMO PRONTO DE REALIZAR VALIDAÇÃO CUSTOMIZADA. SPRING, NESTJS E ASP.NET CORE MVC TÊM.
Lembre que aqui você precisa receber uma data como argumento e, em geral, o seu framework não vai saber automaticamente qual formato ele deve se basear para pegar o texto e transformar para um objeto que represente a data em si na sua linguagem. Você deve precisar configurar.
Utilize um insomnia ou qualquer outra forma para verificar o endpoint
PEGUE CADA UMA DAS CLASSES QUE VOCÊ CRIOU E REALIZE A CONTAGEM DA CARGA INTRÍNSECA. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.
COMO ALBERTO FARIA ESSE CÓDIGO?

**informações de suporte para a combinação Java/Kotlin + Spring**    
Para receber os dados da request como json, temos a annotation @RequestBody
Usamos a annotation @Valid para pedir que os dados da request sejam validados
Para realizar as validações padrões existe a Bean Validation
Se você tiver um atributo do tipo LocalDate,LocalDateTime etc e tiver recebendo os dados como JSON, vai precisar usar a annotation @JsonFormat(pattern = "padrao da data aqui", shape = Shape.STRING)​
Se você tiver recebendo os dados da maneira tradicional, ou seja via form-url-encoded vai precisar usar a annotation @DateTimeFormat
COMO CRIAR UM @RESTCONTROLLERADVICE PARA CUSTOMIZAR O JSON DE SAÍDA COM ERROS DE VALIDAÇÃO

**sensações**    
Aqui, mesmo com muito mais informações, você deve ter tido de novo um pouco daquele sentimento robótico. E aí a gente se questiona, mas não é um trabalho criativo? Não o tempo todo. Não só em desenvolvimento de software, como em qualquer outro trabalho considerado criativo, os momentos onde você vai realmente precisa combinar conhecimentos de uma forma diferente para sair com uma solução da cartola são escassos. O que você precisa estar é preparado(a)! 

O código estar ficando mais fácil é um sinal que você está dominando mais as habilidades necessárias para fazer api's web, o framework, a linguagem etc. Quando aparecer uma funcionalidade mais complicada, você vai ter mais chance de performar melhor.


### Cadastro de país e estados do país
**necessidades**

Precisamos de um cadastro simples de países e seus respectivos estados.

Cada país tem um nome e cada estado tem um nome e pertence a um país.

**restrições para país**
- o nome é obrigatório
- o nome é único

**restrição para estados**
- o nome é obrigatório
- o nome é único
- o país é obrigatório

**resultado esperado**

Dois endpoints para que seja possível cadastrar países e estados. Pode existir país sem estados associados.

Caso alguma restrição não seja atendida, retornar 400 e json com os problemas de validação.

**sobre a utilização do material de suporte aqui**

Aqui você tem mais uma oportunidade de treinar uma operação similar a que você já encontrou. Essa é uma coisa que acontece regularmente na nossa vida trabalhando. Muitas vezes nos pegamos implementando códigos que já são mais usuais para a gente e achamos fácil. Achar fácil, ao contrário do que pode parecer é bom :). Quando está fácil, a chance é que você já tenha internalizado aquele conhecimento. E aí você pode se desafiar! Acha que é fácil? O quão rápido mantendo a qualidade você consegue fazer?

**informações de suporte geral**

COLOQUE UM CRONÔMETRO, ESTIME O TEMPO PARA FAZER E SE DESAFIE.

COMO ALBERTO FARIA ESSE CÓDIGO?

### Começo do fluxo de pagamento - parte1
**necessidades**

Uma coisa importante. Na cdc, você não faz um cadastro e tem suas compras associadas. Toda vez você coloca seu email, cpf/cnpj etc. Como isso vai ser implementado depende da aplicação.

Os seguintes campos precisam ser preenchidos:
- email
- nome
- sobrenome
- documento(cpf/cnpj)
- endereco
- complemento
- cidade
- pais
- estado(caso aquele pais tenha estado)
- telefone
- cep

**restrição**

email obrigatório e com formato adequado
nome obrigatório
sobrenome obrigatório
documento(cpf/cnpj) obrigatório e só precisa ser um cpf ou cnpj
endereco obrigatório
complemento obrigatório
cidade obrigatório
país obrigatório
se o país tiver estados, um estado precisa ser selecionado
estado(caso aquele pais tenha estado) - apenas se o país tiver cadastro de estados
telefone obrigatório
cep é obrigatório

**resultado esperado**

Compra parcialmente gerada, mas ainda não gravada no banco de dados. Falta os dados do pedido em si que vão ser trabalhados no próximo cartão.

**sobre a utilização do material de suporte aqui**

Este começo de fechamento de compra envolve muitos passos. Decidimos começar pegando apenas os dados do formulário relativo a pessoa que está comprando. 

Este é um formulário um pouco mais desafiador, já que possuímos algumas validações customizadas que precisam ser feitas. Não tem nada que você não tenha trabalhado até aqui, mas é mais uma chance de você treinar sua habilidade para conhecer mais das tecnologias e colocar em prática alguns dos pilares que vem nos norteando. ​

**informações de suporte para a feature**

A PRIORIDADE DO CÓDIGO É FUNCIONAR. Se você tentar implementar tudo necessário para criar a versão inicial da compra, vai demorar muito para ver seu código rodando a primeira vez. Lembre que quanto mais você demora de rodar, maior é a chance de ter mais de um problema na primeira execução. OLHE TAMBÉM ESTE OUTRO VÍDEO SOBRE A IMPORTÂNCIA DE PRIORIZAR O FUNCIONAMENTO DO CÓDIGO

CONTROLLERS 100% COESOS para lembrar você a nossa ideia de ter controllers que utilizam todos os atributos.

Como foi que você fez para receber os dados da requisição? Será que aproveitou a facilidade do framework e recebeu a sua entidade(objeto que faz parte do domínio) direto no método mapeado para um endereço? DÁ UMA OLHADA NESSE PILAR AQUI.

Dado que você separou os dados que chegam da request do objeto de domínio, como vai fazer para converter dessa entrada para o domínio? SUGIRO OLHAR UM POUCO SOBRE NOSSA IDEIA DE FORM VALUE OBJECTS. Esse aqui é um formulário bem mais complexo, pois provavelmente vai possuir muito mais dependências. Vai ser um belo desafio.

Muitos dos problemas de uma aplicação vem do fato dela trabalhar com objetos em estado inválido. O ponto mais crítico em relação a isso é justamente quando os dados vêm de outra fonte, por exemplo um cliente externo. É por isso que temos o seguinte pilar: quanto mais externa é a borda mais proteção nós temos. Confira uma explicação sobre ele AQUI e depois AQUI

FAVORECEMOS A COESÃO ATRAVÉS DO ENCAPSULAMENTO. Como você planeja validar se o documento é válido?

Utilize um insomnia ou qualquer outra forma para verificar o endpoint

PEGUE CADA UMA DAS CLASSES QUE VOCÊ CRIOU E REALIZE A CONTAGEM DA CARGA INTRÍNSECA. Esse é o viés de design que estamos trabalhando. Precisamos nos habituar a fazer isso para que se torne algo automático na nossa vida.

COMO ALBERTO FARIA ESSE CÓDIGO?

UMA OUTRA SOLUÇÃO PARA A VALIDAÇÃO DO DOCUMENTO DO USUÁRIO.

### Construa os testes relativos ao código escrito para o desafio
https://dev-eficiente.memberkit.com.br/111379-design-e-arquitetura-de-software-para-devs-eficientes/2674004-construa-os-testes-relativos-ao-codigo-escrito-para-o-desafio

#### 📌 Pré-requisito  

Você precisa ter consumido uma teoria equivalente à combinação dos dois módulos de testes:  

1. **Testes Automatizados Reveladores de Bugs**  
2. **Testes de Software para Devs: Princípios, Práticas, e Dicas Direto das Trincheiras**  

---

#### 🎯 Descrição do Sistema de Testes  

O sistema de testes sugerido combina diferentes tipos de testes dentro da **pirâmide de testes**, utilizando **testes de unidade** e **testes de API** para maximizar a cobertura e detectar bugs rapidamente.  

##### 🔍 Recapitulando o nosso sistema:  

1️⃣ **Testes de unidade automatizados**  
   - Devem cobrir **todos os métodos com branches ou condicionais explícitas**.  
   - Combinação de técnicas para aumentar a qualidade dos testes.  

2️⃣ **Testes de API**  
   - Uso de técnicas como **Boundary Testing** e **Property Based Testing**.  
   - Exploração dos caminhos completos do sistema com o máximo de valores possível.  

3️⃣ **Self Testing**  
   - Os métodos testam suas **pré-condições e pós-condições** automaticamente.  
   - Interrupção do fluxo de execução caso parâmetros ou estados estejam inconsistentes.  

📌 **Importante:** Essa é apenas uma sugestão. O essencial é ter um **sistema claro** para criar seus testes automatizados.  

---

#### 🚀 Desafio  

Agora, crie os testes para o projeto **Casa do Código**.  

- **Siga o sistema sugerido** ou defina um próprio.  
- **Não cubra linhas aleatoriamente**, tenha um critério bem definido.  
- Use o **feeling** para complementar uma abordagem já estabelecida.  

---

#### 📚 Informações de Suporte  

✅ **Existe expressão condicional com valores?** → Utilize **Boundary Testing**.  

✅ **Existe expressão condicional ou branch escrita por você?** → Utilize **MC/DC**.  

✅ **Passou da borda externa?**  
   - Os valores de parâmetros e estado do sistema devem estar válidos.  
   - **Garanta pré-condições com Self Testing**.  

✅ **Sobrou código sem branch ou expressão condicional?**  
   - Ele pode ser alcançado através de testes mais integrados?  

✅ **Sobrou código sem branch ou expressão condicional que não foi testado por unidade?**  
   - **Talvez seja o caso de um Teste de API**, possivelmente combinado com **Property Based Testing**.  

---

#### ✅ Testes Automatizados Criados  

##### 📌 Testes de unidade  

- Testes relativos à classe **NovoLivroRequest** → [CONFIRA AQUI](#)  
- Testes relativos à classe **Cupom** → [CONFIRA AQUI](#)  
- Testes relativos à classe **NovaCompraRequest** → [CONFIRA AQUI](#)  
- Testes relativos à classe **Pedido** → [CONFIRA AQUI](#)  
- Testes relativos à classe **CupomValidoValidator** → [CONFIRA AQUI](#)  
- Testes relativos à classe **EstadoPertencePaisValidator** → [CONFIRA AQUI](#)  

##### 📌 Testes de API  

- Explicação inicial sobre suporte do framework a testes de API → [CONFIRA AQUI](#)  
- Testes de API **Autores** → [CONFIRA AQUI](#)  
- Testes de API **Categorias** → [CONFIRA AQUI](#)  
- Testes de API **Livros** → [CONFIRA AQUI](#)  
- Testes de API **Detalhe do livro** → [CONFIRA AQUI](#)  
- Testes de API **País e estados** → [CONFIRA AQUI](#)  
- Testes de API **FechamentoCompra** → [CONFIRA AQUI](#)  
- Testes de API **Cupom** → [CONFIRA AQUI](#)  

##### 📌 Cobertura dos Testes  
- Vamos agora olhar nossa cobertura → [CONFIRA AQUI](#)  
