import './UserReviewComponent.css';

const UserReviewComponent = () => 
{
    return (
        <div className="layout_User_Review">   
            <div className="box_User_Review">
                <table className="table_User_Review">
                    <tr>
                        <td>
                            작성자
                        </td>
                        <td>
                            eee
                        </td>
                    </tr>
                    <tr>
                        <td>
                            별점
                        </td>
                        <td>
                            *****
                        </td>
                    </tr>
                    <tr>
                        <td>
                            내용
                        </td>
                        <td>
                            좋다
                        </td>
                    </tr>
                </table>
                <div>
                    <button
                        className="btn_Review">
                        등록
                    </button>
                    <button
                        className="btn_Review">
                        취소
                    </button>
                </div>
            </div>
        </div>
    );
}

export default UserReviewComponent;