import { Component } from '@angular/core';
import {Article, ArticleService} from '../../../services/generated';
import {NgIf} from '@angular/common';
import {PaginationComponent} from '../components/pagination/pagination.component';
import {SearchBarComponent} from '../components/search-bar/search-bar.component';
import {SpinnerComponent} from '../components/spinner.component';
import {SearchResultsComponent} from "../components/search-results/search-results.component";

@Component({
  selector: 'app-search-page',
  imports: [
    PaginationComponent,
    SearchBarComponent,
    NgIf,
    SpinnerComponent,
    SearchResultsComponent
  ],
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent {
  query = '';
  articles: Article[] = [];
  totalPages = 0;
  totalElements = 0;
  currentPage = 0;
  pageSize = 20;
  isLoading = false;
  errorMessage = '';

  constructor(private readonly articleService: ArticleService) {}

  onSearch(query: any) {
    this.query = query;
    this.currentPage = 0; // Reset to the first page
    this.fetchArticles();
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.fetchArticles();
  }

  private fetchArticles() {
    this.isLoading = true;
    this.errorMessage = '';
    this.articleService
      .searchArticles(this.query, this.currentPage, this.pageSize)
      .subscribe({
        next: (response) => {
          console.log(response);
          this.articles = response.content;
          this.totalPages = response.totalPages;
          this.totalElements = response.totalElements;
          this.isLoading = false;
        },
        error: () => {
          this.errorMessage = 'Failed to load articles. Please try again.';
          this.isLoading = false;
        },
      });
  }

  protected readonly Math = Math;
}
