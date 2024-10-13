import { IsNotEmpty, IsNumber, IsString } from 'class-validator';


export class UpdateBoardRequest {
  @IsNotEmpty()
  @IsString()
  title: string;
  content: string;
}

export class CreateBoardRequest extends UpdateBoardRequest {
}

/**
 * board 식별 파라미터
 */
export class BoardIdParams {
  @IsNotEmpty()
  @IsNumber()
  id: number
}