import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Prioridade, Situacao } from '../models/tarefa.enums';
import { Tarefa, TarefaPage, TarefaResponse } from '../models/tarefa.dto';

@Injectable({
  providedIn: 'root'
})
export class TarefaService {

  private apiUrl = 'http://localhost:8080/tarefas';

  constructor(private http: HttpClient) {}

  create(tarefa: Tarefa): Observable<TarefaResponse> {
    return this.http.post<TarefaResponse>(this.apiUrl, tarefa);
  }

  getAll(
    nome?: string,
    prioridade?: Prioridade,
    situacao?: Situacao,
    page: number = 0,
    size: number = 10,
    sort: string = 'nome'
  ): Observable<TarefaPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (nome) params = params.set('nome', nome);
    if (prioridade) params = params.set('prioridade', prioridade);
    if (situacao) params = params.set('situacao', situacao);

    return this.http.get<TarefaPage>(this.apiUrl, { params });
  }

  getById(id: string): Observable<TarefaResponse> {
    return this.http.get<TarefaResponse>(`${this.apiUrl}/${id}`);
  }

  update(id: string, tarefa: Tarefa): Observable<TarefaResponse> {
    return this.http.put<TarefaResponse>(`${this.apiUrl}/${id}`, tarefa);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  marcarComoCompleta(id: string): Observable<TarefaResponse> {
    return this.http.patch<TarefaResponse>(`${this.apiUrl}/${id}/concluir`, {});
  }

  marcarComoPendente(id: string): Observable<TarefaResponse> {
    return this.http.patch<TarefaResponse>(`${this.apiUrl}/${id}/pendente`, {});
  }

}
