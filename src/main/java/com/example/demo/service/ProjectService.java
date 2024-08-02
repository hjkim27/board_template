package com.example.demo.service;

import com.example.demo.bean.dto.project.ProjectCommentDTO;
import com.example.demo.bean.dto.project.ProjectIssueDTO;
import com.example.demo.bean.dto.project.ProjectLabelDTO;
import com.example.demo.bean.dto.project.ProjectRepositoryDTO;
import com.example.demo.bean.vo.project.ProjectCommentVO;
import com.example.demo.bean.vo.project.ProjectIssueVO;
import com.example.demo.bean.vo.project.ProjectLabelVO;
import com.example.demo.bean.vo.project.ProjectRepositoryVO;
import com.example.demo.mapper.first.ProjectCommentMapper;
import com.example.demo.mapper.first.ProjectIssueMapper;
import com.example.demo.mapper.first.ProjectLabelMapper;
import com.example.demo.mapper.first.ProjectRepositoryMapper;
import com.example.demo.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 *     git 연동 관련 로직을 정리한 service 클래스
 * </pre>
 *
 * @author hjkim27
 * @since 24.07.28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectLabelMapper projectLabelMapper;
    private final ProjectRepositoryMapper projectRepositoryMapper;
    private final ProjectIssueMapper projectIssueMapper;
    private final ProjectCommentMapper projectCommentMapper;
    private final ModelMapper modelMapper;

    /**
     * <pre>
     *     git label link
     *     - 삭제여부 확인을 위해 모든 row 를 active:false 업데이트
     *     - label_id 를 기준으로 존재여부를 확인, insert/update 진행
     * </pre>
     *
     * @param labelDTOList
     */
    public void insertLabels(List<ProjectLabelDTO> labelDTOList) {
        projectLabelMapper.updateActiveFalse();
        for (ProjectLabelDTO dto : labelDTOList) {
            ProjectLabelVO vo = modelMapper.map(dto, ProjectLabelVO.class);

            boolean isExistLabel = projectLabelMapper.isExistRow(vo.getLabelId());
            if (isExistLabel) {
                projectLabelMapper.updateRow(vo);
                log.info("update >> labelId : {}", vo.getLabelId());
            } else {
                projectLabelMapper.insertRow(vo);
                log.info("insert >> labelId : {}", vo.getLabelId());
            }
        }
    }


    /**
     * <pre>
     *     git repository link
     *     - 삭제여부 확인을 위해 모든 row 를 active:false 업데이트
     *     - full_name 을 기준으로 존재여부 확인, insert/update 진행
     * </pre>
     *
     * @param repositoryDTOList
     * @since 24.07.30
     */
    public void insertRepos(List<ProjectRepositoryDTO> repositoryDTOList) {
        projectRepositoryMapper.updateActiveFalse();
        for (ProjectRepositoryDTO dto : repositoryDTOList) {
            ProjectRepositoryVO vo = modelMapper.map(dto, ProjectRepositoryVO.class);

            boolean isExistRepo = projectRepositoryMapper.isExistRow(vo);
            if (isExistRepo) {
                projectRepositoryMapper.updateRow(vo);
                log.info("update >> fullName : {}", vo.getFullName());
            } else {
                projectRepositoryMapper.insertRow(vo);
                log.info("insert >> fullName : {}", vo.getFullName());
            }

            // issue 연동
            List<ProjectIssueDTO> issues = dto.getIssueDTOList();
            insertIssues(issues, vo.getSid());
        }
    }

    /**
     * <pre>
     *     git issue link
     *     - issue_number 를 기준으로 존재여부 확인, insert/update 진행
     * </pre>
     *
     * @param issueDTOList
     * @param repositorySid
     * @since 24.08.02
     */
    public void insertIssues(List<ProjectIssueDTO> issueDTOList, Integer repositorySid) {
        for (ProjectIssueDTO dto : issueDTOList) {
            ProjectIssueVO vo = modelMapper.map(dto, ProjectIssueVO.class);
            vo.setRepositorySid(repositorySid);
            vo.setLabelIds(FormatUtil.listToString(dto.getLabelLIdList(), ","));

            boolean isExistIssue = projectIssueMapper.isExistRow(vo);
            if (isExistIssue) {
                projectIssueMapper.updateRow(vo);
                log.info("update >> issueNumber : {}", vo.getIssueNumber());
            } else {
                projectIssueMapper.insertRow(vo);
                log.info("insert >> issueNumber : {}", vo.getIssueNumber());
            }

            // comment 연동
            List<ProjectCommentDTO> comments = dto.getCommentList();
            insertComment(comments);
        }
    }

    /**
     * <pre>
     *     git comment link
     *     - 삭제여부 확인을 위해 모든 row 를 active:false 업데이트
     *     - comment_id 를 기준으로 존재여부 확인, insert/update 진행
     * </pre>
     *
     * @param commentDTOList
     * @since 24.08.02
     */
    public void insertComment(List<ProjectCommentDTO> commentDTOList) {
        projectCommentMapper.updateActiveFalse();
        for (ProjectCommentDTO dto : commentDTOList) {
            ProjectCommentVO vo = modelMapper.map(dto, ProjectCommentVO.class);

            boolean isExistComment = projectCommentMapper.isExistRow(vo);
            if (isExistComment) {
                projectCommentMapper.updateRow(vo);
                log.info("update >> commentId : {}", vo.getCommentId());
            } else {
                projectCommentMapper.insertRow(vo);
                log.info("insert >> commentId : {}", vo.getCommentId());
            }
        }
    }

}
