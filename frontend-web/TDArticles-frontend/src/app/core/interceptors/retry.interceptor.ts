import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { retry, timer } from 'rxjs';

const MAX_RETRIES = 3;
const INITIAL_DELAY = 1000;

const isRetryable = (error: HttpErrorResponse): boolean => {
  if (error instanceof HttpErrorResponse) {
    return error.status === 0 || error.status === 408 || error.status === 500;
  }
  return false;
};

export const retryInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    retry({
      count: MAX_RETRIES,
      delay: (error, retryCount) => {
        if (!isRetryable(error)) {
          throw error;
        }
        console.log(`Retrying request attempt ${retryCount}`);
        return timer(INITIAL_DELAY * Math.pow(2, retryCount - 1));
      },
    })
  );
};