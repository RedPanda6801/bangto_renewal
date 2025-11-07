import React from "react";
import { useNavigate } from 'react-router-dom';
import './UserQNAComponent.css';

const UserQNAComponent = () => 
{
    const navigate = useNavigate();

    return (
        <div className="layout_Item_QNA_Contents">                 
            <div className="box_Item_QNA_Regi">
                <table className="table_Item_QNA_Regi">
                    <tr>
                        <td colSpan={2}>
                            문의자
                        </td>
                    </tr>
                    <tr>
                        <td>
                            내용
                        </td>
                        <td>
                            문의 내용 작성 칸
                        </td>
                    </tr>
                </table>
                <div>
                    <button
                        className="btn_QNA">
                        등록
                    </button>
                    <button
                        className="btn_QNA"
                        onClick={() => navigate(-1)}>
                        취소
                    </button>
                </div>
            </div>
        </div>
    );
}

export default UserQNAComponent;