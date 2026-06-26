export interface MlResult {
  predictions: number[];
  metrics: Record<string, number>;
}
