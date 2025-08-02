import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { TarefaResponse } from '../../models/tarefa.dto';
import { TarefaService } from '../../services/tarefa-service';
import { Prioridade, Situacao } from '../../models/tarefa.enums';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSort, MatSortModule, Sort } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-lista-tarefa',
  templateUrl: './lista-tarefa.html',
  styleUrl: './lista-tarefa.scss',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatTooltipModule
  ]
})
export class ListaTarefa implements AfterViewInit, OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['nome', 'prioridade', 'situacao', 'dataPrevistaConclusao', 'acoes'];
  dataSource = new MatTableDataSource<TarefaResponse>();

  prioridades = Object.values(Prioridade);
  situacoes = Object.values(Situacao);

  nomeFilter = new FormControl('');
  prioridadeFilter = new FormControl('');
  situacaoFilter = new FormControl('');

  totalItems = 0;
  pageSize = 10;
  currentPage = 0;
  sortField = 'nome';
  sortDirection = 'asc';
  pageSizeOptions = [5, 10, 25, 100];

  constructor(
    private tarefaService: TarefaService,
    private toastr: ToastrService
  ) {}

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.sort.sortChange.subscribe(() => {
      this.currentPage = 0;
      this.carregarTarefas();
    });

    this.carregarTarefas();
  }

  ngOnInit(): void {
    this.carregarTarefas();

    this.nomeFilter.valueChanges.subscribe(() => this.carregarTarefas());
    this.prioridadeFilter.valueChanges.subscribe(() => this.carregarTarefas());
    this.situacaoFilter.valueChanges.subscribe(() => this.carregarTarefas());
  }

  carregarTarefas(): void {

    const params = {
      page: this.currentPage,
      size: this.pageSize,
      sort: `${this.sortField},${this.sortDirection}`,
      nome: this.nomeFilter.value || undefined,
      prioridade: this.prioridadeFilter.value || undefined,
      situacao: this.situacaoFilter.value || undefined
    };

    this.tarefaService.getAll(
      params.nome,
      params.prioridade as Prioridade,
      params.situacao as Situacao,
      params.page,
      params.size,
      params.sort
    ).subscribe({
      next: (page) => {
        this.dataSource.data = page.content;
        this.totalItems = page.totalElements;

        if (this.paginator) {
          this.paginator.length = this.totalItems;
          this.paginator.pageIndex = this.currentPage;
          this.paginator.pageSize = this.pageSize;
        }
      },
      error: (err) => this.toastr.error('Erro ao carregar tarefas')
    });

  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.carregarTarefas();
  }

  onSortChange(event: Sort): void {
    this.sortField = event.active;
    this.sortDirection = event.direction;
    this.carregarTarefas();
  }

  deleteTarefa(id: string): void {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.tarefaService.delete(id).subscribe({
        next: () => {
          this.toastr.success('Tarefa excluída com sucesso');
          this.carregarTarefas();
        },
        error: (err) => this.toastr.error('Erro ao excluir tarefa')
      });
    }
  }

  marcarComoCompleta(id: string): void {
    this.tarefaService.marcarComoCompleta(id).subscribe({
      next: () => {
        this.toastr.success('Tarefa marcada como concluída');
        this.carregarTarefas();
      },
      error: (err) => this.toastr.error('Erro ao atualizar tarefa')
    });
  }

  marcarComoPendente(id: string): void {
    this.tarefaService.marcarComoPendente(id).subscribe({
      next: () => {
        this.toastr.success('Tarefa marcada como pendente');
        this.carregarTarefas();
      },
      error: (err) => this.toastr.error('Erro ao atualizar tarefa')
    });
  }

}
