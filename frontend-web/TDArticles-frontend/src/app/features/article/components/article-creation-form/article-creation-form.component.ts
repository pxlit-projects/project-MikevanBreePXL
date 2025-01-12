import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { Article } from '../../../../shared/models/article.model';
import { AuthService } from '../../../auth/services/auth.service';
import { BehaviorSubject } from 'rxjs';
import { AsyncPipe } from '@angular/common';

interface ArticleForm {
  id: number;
  title: string;
  content: string;
  author: string;
  concept: boolean;
}

type FormField = keyof ArticleForm;

@Component({
  selector: 'app-article-creation-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    AsyncPipe
  ],
  templateUrl: './article-creation-form.component.html',
  styleUrl: './article-creation-form.component.css'
})
export class ArticleCreationFormComponent implements OnInit {
  @Input() hideConceptCheckbox = false;
  @Input() set selectedArticle(article: Article | null) {
    if (article) {
      this.articleForm.patchValue({
        id: article.id,
        title: article.title,
        content: article.content,
        author: article.author,
        concept: article.concept
      });
      this.formTitle$.next('Edit Article');
      this.formSave$.next("Save");
    }
  }
  @Output() submitArticle = new EventEmitter<Article>();
  @Output() cancelEdit = new EventEmitter<void>();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.articleForm = this.createForm();
  }
  
  formTitle$ = new BehaviorSubject<string>('Create New Article');
  articleForm: FormGroup;
  formErrors: Record<FormField, string> = {
    id: '',
    title: '',
    content: '',
    author: '',
    concept: ''
  };
  formSave$ = new BehaviorSubject<string>("Create Article");

  validationMessages: Record<FormField, Record<string, string>> = {
    id: {},
    title: {
      required: 'Title is required'
    },
    content: {
      required: 'Content is required'
    },
    author: {},
    concept: {
      required: 'Concept status is required'
    }
  };

  ngOnInit(): void {
    this.articleForm.patchValue({
      author: this.authService.getCurrentUser()?.name,
      concept: false // Set default value
    });
    this.subscribeToFormChanges();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      id: [''],
      title: ['', [Validators.required]],
      content: ['', [Validators.required]],
      author: ['', [Validators.required]],
      concept: [false]
    });
  }

  private subscribeToFormChanges(): void {
    Object.keys(this.articleForm.controls).forEach((key) => {
      const control = this.articleForm.get(key);
      if (control) {
        control.valueChanges.subscribe(() => {
          this.onControlValueChanged(key as FormField);
        });
      }
    });
  }

  private onControlValueChanged(field: FormField): void {
    const control = this.articleForm.get(field);
    this.formErrors[field] = '';

    if (control?.invalid && (control.dirty || control.touched)) {
      const messages = this.validationMessages[field];
      for (const key in control.errors) {
        this.formErrors[field] += messages[key] + ' ';
      }
    }
  }
  
  resetArticleForm() {
    if (this.articleForm.value.id) {
      this.cancelEdit.emit();
    }
    this.articleForm.reset();
    this.articleForm.patchValue({
      author: this.authService.getCurrentUser()?.name,
      concept: false // Set default value
    });
    this.subscribeToFormChanges();
  }

  onSubmit(): void {
    if (!this.articleForm?.valid) {
      return;
    }

    const formValue = this.articleForm.value;
    const articleData: Article = {
      id: formValue.id,
      title: formValue.title,
      content: formValue.content,
      author: formValue.author,
      concept: formValue.concept ?? false // Set default if undefined
    };

    this.submitArticle.emit(articleData);
  }

  // Add helper method for form validation state
  isFieldInvalid(fieldName: string): boolean {
    const field = this.articleForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }
}