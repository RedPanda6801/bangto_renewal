import { useNavigate } from 'react-router-dom';
import './StoreQNADetailComponent.css';

const StoreQNADetailComponent = () => 
{
    const navigate = useNavigate();

    return (
        <div className="layout_QNA">
            <div className="box_QNA">              
                <table className="table_QNA">
                    <tr>
                        <td>문의자</td>
                        <td>문의자임</td>
                    </tr>
                    <tr>
                        <td>문의 내용</td>
                        <td>문의 내용</td>
                        <td>2025/02/24</td>
                    </tr>
                    <tr>
                        <td>답변</td>
                        <td>답변 내용</td>
                        <td>2025/02/25</td>
                    </tr>
                </table>
                <div>
                    <input 
                        className="btn_QNA"
                        type="button" 
                        value="저장"/>
                    <input 
                        className="btn_QNA"
                        type="button" 
                        value="취소"
                        onClick={() => navigate(-1)}/>
                </div>
            </div>
        </div>
    );
}

export default StoreQNADetailComponent;