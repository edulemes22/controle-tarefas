import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule  } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule  } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Prioridade } from '../../models/tarefa.enums';
import { Tarefa } from '../../models/tarefa.dto';
import { TarefaService } from '../../services/tarefa-service';
import { dateNotInPast } from '../../utils/validators';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { CommonModule } from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter } from '@angular/material-moment-adapter';
import moment from 'moment';

export const DATA_FORMATADA = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'app-formulario-tarefa',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDatepickerModule
  ],
  templateUrl: './formulario-tarefa.html',
  styleUrl: './formulario-tarefa.scss',
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    { provide: MAT_DATE_FORMATS, useValue: DATA_FORMATADA },
  ]
})
export class FormularioTarefa implements OnInit {

  tarefaForm: FormGroup;
  isEditMode = false;
  tarefaId: string | null = null;
  prioridades = Object.values(Prioridade);

  dateFilter = (d: Date | null): boolean => {
    if (!d) return false;
    const date = moment(d);
    const today = moment().startOf('day');
    return date.isSameOrAfter(today);
  }

  constructor(
    private fb: FormBuilder,
    private tarefaService: TarefaService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.tarefaForm = this.fb.group({
      nome: ['', Validators.required],
      descricao: [''],
      prioridade: [Prioridade.BAIXA, Validators.required],
      dataPrevistaConclusao: ['', [Validators.required, dateNotInPast]]
    });
  }

  ngOnInit(): void {
    this.tarefaId = this.route.snapshot.paramMap.get('id');

    if (this.tarefaId) {
      this.isEditMode = true;
      this.carregarTarefa(this.tarefaId);
    }
  }

  carregarTarefa(id: string): void {
    this.tarefaService.getById(id).subscribe({
      next: (tarefa) => {
        this.tarefaForm.patchValue({
          nome: tarefa.nome,
          descricao: tarefa.descricao,
          prioridade: tarefa.prioridade,
          dataPrevistaConclusao: tarefa.dataPrevistaConclusao
        });
      },
      error: (err) => this.toastr.error('Erro ao carregar tarefa')
    });
  }

  onSubmit(): void {
    if (this.tarefaForm.invalid) {
      this.tarefaForm.markAllAsTouched();
      return;
    }

    const tarefaData: Tarefa = this.tarefaForm.value;

    if (this.isEditMode && this.tarefaId) {
      this.tarefaService.update(this.tarefaId, tarefaData).subscribe({
        next: () => {
          this.toastr.success('Tarefa atualizada com sucesso');
          this.router.navigate(['/']);
        },
        error: (err) => this.toastr.error('Erro ao atualizar tarefa')
      });
    } else {
      this.tarefaService.create(tarefaData).subscribe({
        next: () => {
          this.toastr.success('Tarefa criada com sucesso');
          this.router.navigate(['/']);
        },
        error: (err) => this.toastr.error('Erro ao criar tarefa')
      });
    }
  }

  formatDate(event: any) {
    const date = moment(event.value);
    this.tarefaForm.get('dataPrevistaConclusao')?.setValue(date.format('DD/MM/YYYY'), { emitEvent: false });
  }

}
