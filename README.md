# locagames4all
Repositório do trabalho final da disciplina de Projeto Detalhado de Software.

# Swagger
Documentação da Api estará disponivel via Swagger que ficará disponivel nos links:

http://localhost:8080/api-docs

http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config

Este segundo permite visualizar as entradas esperadas assim como os retornos esperados. Além do mais será possivel testar o envio das requisições pelo próprio Swagger.

# Banco de Dados

#### No projeto encontrará dois perfis de desenvolvimento disponiveis. São eles :

**test** e **prod**.

## Test


No perfil de **test** tem a sua disposição o banco de dados em mémoria H2.

Para acessá-lo basta ir ao link

<http://localhost:8080/h2-console>

Nele você poderá ver os dados e fazer alterações que não serão persistidos quando reiniciar a aplicação,
Então nesse perfil os dados não são persistidos, mas permitem que a aplicação seja testada.

### configuração padrão de acesso H2
`login : sa
senha: `


**Não há senha no h2-console**, mas caso queira mudar os dados, basta acessar o arquivo _application-test.properties_.
## Prod

Já no perfil de **prod** temos como banco de produção o **Postgres**, para acessá-lo é necessário ter o mesmo instalado em sua máquina e criado um banco de dados com o nome "locagames".
É importante que o nome esteja exatamente assim. Sem aspas e somente letras minusculas.
Com essa configuração os dados carregados na aplicação são os dados persistidos no banco e cada operação realizada na aplicação será registrada e persistida mesmo após reiniciar a aplicação.

### configuração padrão de acesso Postgres
`login : postgres
senha:123`


Caso queira mudar os dados, basta acessar o arquivo _application-prod.properties_.


# Mudança entre perfis de desenvolvimento

Dentro do projeto Siga esse caminho



**src->main->resources->application-properties**
procurando por _application-properties_ .

Ao abrir ele terá algo como:
`spring.profiles.active=prod`

Então basta que altere para
**test**  ou **prod** e as configuração serão alteradas.









