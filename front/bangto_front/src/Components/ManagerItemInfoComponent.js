import { useLocation } from 'react-router-dom';
import './ManagerItemInfoComponent.css';

const ManagerItemInfoComponent = () => 
{
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);  

    const storename = searchParams.get("storename");

    return (<div className="layout_Manager_Item_Info">
                <div className="box_Manager_Item_Info">
                    <input 
                        className="Item_Add_Store" 
                        type="text" 
                        defaultValue={storename}
                        disabled/>
                    <table className="table_Manager_Item_Info">
                        <tr>        
                            <td rowSpan={2}>
                                <input
                                    type="image"
                                    className="btn_User_Cart_Img"/>
                            </td>
                            <td>
                                물품 이름
                            </td>
                            <td rowSpan={2}>
                                <button>
                                    수정
                                </button>
                            </td>
                            <td rowSpan={2}>
                                <button>
                                    삭제
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <td>가격</td>
                        </tr>
                    </table>
                    <table className="table_Manager_Item_Info">
                        <tr>        
                            <td rowSpan={2}>
                                <input
                                    type="image"
                                    className="btn_User_Cart_Img"/>
                            </td>
                            <td>
                                물품 이름
                            </td>
                            <td rowSpan={2}>
                                <button>
                                    수정
                                </button>
                            </td>
                            <td rowSpan={2}>
                                <button>
                                    삭제
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <td>가격</td>
                        </tr>
                    </table>
                </div>
                <div>
                </div>
            </div>
    );
}

export default ManagerItemInfoComponent;