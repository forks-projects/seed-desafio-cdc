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

### Cadastro de um novo autor

É necessário cadastrar um novo autor no sistema. Todo autor tem um nome, email e uma descrição. Também queremos saber o instante exato que ele foi registrado.

**Restrições**

- O instante não pode ser nulo
- O email é obrigatório
- O email tem que ter formato válido
- O nome é obrigatório
- A descrição é obrigatória e não pode passar de 400 caracteres 

**Resultado esperado**
- Um novo autor criado e status 200 retornado

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


### Email do autor é único
**necessidades**    
O email do autor precisa ser único no sistema

**resultado esperado**    
Erro de validação no caso de email duplicado

**informações de suporte geral**    
- TODO FRAMEWORK MVC MINIMAMENTE MADURO POSSUI UM MECANISMO PRONTO DE REALIZAR VALIDAÇÃO CUSTOMIZADA. SPRING, NESTJS E ASP.NET CORE MVC TÊM.
- Aqui provavelmente você terá um if em algum lugar para verificar a existência de um outro autor. Todo código que tem uma branch de código(if,else) tem mais chance de executar de maneira equivocada. Tente criar um teste automatizado para aumentar ainda mais a confiabilidade do seu código. CRIAMOS TESTES AUTOMATIZADOS PARA QUE ELE NOS AJUDE A REVELAR E CONSERTAR BUGS NA APLICAÇÃO.​​ 
- COMO ALBERTO FARIA ESSE CÓDIGO?

**informações de suporte para a combinação Java/Kotlin + Spring**    
- Para receber os dados da request como json, temos a annotation @RequestBody
- Usamos a annotation @Valid para pedir que os dados da request sejam validados
- Para realizar as validações padrões existe a Bean Validation
- COMO CRIAR UM @RESTCONTROLLERADVICE PARA CUSTOMIZAR O JSON DE SAÍDA COM ERROS DE VALIDAÇÃO
- COMO EXTERNALIZAR AS MENSAGENS DE ERRO NO ARQUIVO DE CONFIGURAÇÃO.

# Faça um fork desse repositório

Este é um repositório vazio de propósito. A ideia é que você faça um fork para que eu, Alberto, possa usar o github para ter a chance de olhar vários dos códigos produzido por você e seus(as) colegas da Jornada Dev Eficiente :). 

Durante cada uma das seis semanas eu vou pegar código por amostragem e analisar. Feito isso, vou criar um vídeo anonimizando a pessoa que é dona do código, com as minhas observações e postar isso como material de suporte da funcionalidade :). 
