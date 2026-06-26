export interface Experiment {
  experimentId?: number;
  userId: number;
  modelId: number;
  datasetId: number;
  parameters: string;
  accuracy?: number;
  mse?: number;
  r2Score?: number;
  status: string;
  createdAt?: string;
}
