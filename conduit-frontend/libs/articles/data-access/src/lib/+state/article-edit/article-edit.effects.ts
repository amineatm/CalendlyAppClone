import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { formsActions, ngrxFormsQuery } from '@realworld/core/forms';
import { catchError, concatMap, map, of, tap } from 'rxjs';
import { ArticlesService } from '../../services/articles.service';
import { articleEditActions } from './article-edit.actions';
import { articleActions } from '@realworld/articles/data-access/src';

export const publishArticle$ = createEffect(
  (
    actions$ = inject(Actions),
    articlesService = inject(ArticlesService),
    store = inject(Store),
    router = inject(Router),
  ) => {
    return actions$.pipe(
      ofType(articleEditActions.publishArticle),
      concatLatestFrom(() => store.select(ngrxFormsQuery.selectData)),
      concatMap(([_, data]) =>
        articlesService.publishArticle(data).pipe(
          tap((result) => router.navigate(['article', result.article.slug])),
          map(() => articleEditActions.publishArticleSuccess()),
          catchError((result) => of(formsActions.setErrors({ errors: result.error.errors }))),
        ),
      ),
    );
  },
  { functional: true },
);


export const loadArticle$ = createEffect(
  (actions$ = inject(Actions), articlesService = inject(ArticlesService)) => {
    return actions$.pipe(
      ofType(articleActions.loadArticle),
      concatMap((action) =>
        articlesService.getArticle(action.slug).pipe(
          map((response) => articleActions.loadArticleSuccess({ article: response.article })),
          catchError((error) => of(articleActions.loadArticleFailure(error))),
        ),
      ),
    );
  },
  { functional: true },
);
