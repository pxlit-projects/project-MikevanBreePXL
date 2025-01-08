import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Article } from '../../../shared/models/article.model';
import { environment } from '@env/environment'

interface FormErrorMessages {
  title: string;
  content: string;
  author: string;
  concept: string;
}

type FormField = keyof FormErrorMessages;

@Component({
  selector: 'app-article-creation-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule
  ],
  templateUrl: './article-creation-form.component.html',
  styleUrl: './article-creation-form.component.css'
})
export class ArticleCreationFormComponent {
  @Input() set selectedArticle(article: Article | null) {
    if (article) {
      this.articleForm.patchValue({
        title: article.title,
        content: article.content,
        author: article.author,
        concept: article.concept
      });
    }
  }
  @Output() submitArticle = new EventEmitter<Article>();

  constructor(
    private fb: FormBuilder, 
    private http: HttpClient,
    private router: Router
  ) {
    this.articleForm = this.createForm();
  }

  articleForm: FormGroup;
  formErrors: FormErrorMessages = {
    title: '',
    content: '',
    author: '',
    concept: ''
  };

  validationMessages: Record<FormField, { [key: string]: string }> = {
    title: { required: 'Title is required' },
    content: { required: 'Content is required' },
    author: { required: 'Author is required' },
    concept: { required: 'Concept status is required' }
  };

  ngOnInit(): void {
    this.subscribeToFormChanges();
    // Add form state debugging
    this.articleForm.statusChanges.subscribe(status => {
      console.log('Form Status:', status);
      console.log('Form Values:', this.articleForm.value);
      console.log('Form Valid:', this.articleForm.valid);
      console.log('Form Errors:', this.articleForm.errors);
    });
  }

  private createForm(): FormGroup {
    return this.fb.group({
      title: ['', [Validators.required]],
      content: ['', [Validators.required]],
      author: ['', [Validators.required]],
      authorId: [''],
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
      if (control.errors) {
        Object.keys(control.errors).forEach(errorKey => {
          if (messages[errorKey]) {
            this.formErrors[field] += messages[errorKey];
          }
        });
      }
    }
  }

  onSubmit(): void {
    if (!this.articleForm?.valid) {
      return;
    }

    this.http.post(`${environment.apiPostUrl}create`, this.articleForm.value)
      .subscribe({
        next: () => {
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Error creating article:', error);
        }
      });
  }

  // Add helper method for form validation state
  isFieldInvalid(fieldName: string): boolean {
    const field = this.articleForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }
}