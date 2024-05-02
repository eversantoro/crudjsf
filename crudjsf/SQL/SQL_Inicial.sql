-- Tabela Pessoa
CREATE TABLE pessoa (
    id INT PRIMARY KEY,
    nome VARCHAR(150),
    idade DATE,
    sexo VARCHAR(2)
);

-- Tabela Endereco
CREATE TABLE endereco (
    id INT PRIMARY KEY,
    estado VARCHAR(2),
    cidade VARCHAR(100),
    logradouro VARCHAR(100),
    numero INT,
    cep VARCHAR(8),
    id_pessoa INT,
    FOREIGN KEY (id_pessoa) REFERENCES pessoa(id)
);
