import { Prioridade, Situacao } from './tarefa.enums';

export interface Tarefa {
  id?: string;
  nome: string;
  descricao?: string;
  prioridade: Prioridade;
  situacao: Situacao;
  dataPrevistaConclusao: string;
}

export interface TarefaResponse {
  id: string;
  nome: string;
  descricao?: string;
  prioridade: Prioridade;
  situacao: Situacao;
  dataPrevistaConclusao: string;
  dataCriacao: string;
}

export interface TarefaPage {
  content: TarefaResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
