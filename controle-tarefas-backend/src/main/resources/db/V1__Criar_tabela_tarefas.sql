CREATE TABLE tarefas (
                         id UUID PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         descricao VARCHAR(500),
                         prioridade VARCHAR(10) NOT NULL,
                         situacao VARCHAR(15) NOT NULL,
                         data_prevista_conclusao DATE NOT NULL,
                         data_criacao TIMESTAMP NOT NULL
);

CREATE INDEX idx_tarefas_nome ON tarefas (nome);
CREATE INDEX idx_tarefas_prioridade ON tarefas (prioridade);
CREATE INDEX idx_tarefas_situacao ON tarefas (situacao);
CREATE INDEX idx_tarefas_data_prevista ON tarefas (data_prevista_conclusao);