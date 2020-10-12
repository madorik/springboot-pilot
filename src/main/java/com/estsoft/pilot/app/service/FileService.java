package com.estsoft.pilot.app.service;

import com.estsoft.pilot.app.config.PilotProperties;
import com.estsoft.pilot.app.domain.entity.FileEntity;
import com.estsoft.pilot.app.domain.repository.FileRepository;
import com.estsoft.pilot.app.dto.FileDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by madorik on 2020-09-28
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class FileService {

    private final PilotProperties pilotProperties;

    private final FileRepository fileRepository;

    private FileDto convertEntityToDto(FileEntity fileEntity) {
        return FileDto.builder()
                .id(fileEntity.getId())
                .fileName(fileEntity.getFileName())
                .saveFileName(fileEntity.getSaveFileName())
                .filePath(fileEntity.getFilePath())
                .contentType(fileEntity.getContentType())
                .size(fileEntity.getSize())
                .boardId(fileEntity.getBoardId())
                .commentId(fileEntity.getCommentId())
                .createdDate(fileEntity.getCreatedDate())
                .modifiedDate(fileEntity.getModifiedDate())
                .build();
    }

    /**
     * fileName : test.jpg
     * filePath : d:/images/uuid-test.jpg
     * saveFileName : uuid-test.png
     * contentType : image/jpeg
     * size : 4994942
     *
     * @param file file
     * @return FileEntity
     * @throws IOException IOException
     */
    public FileEntity store(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new FileNotFoundException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path rootLocation = Paths.get(pilotProperties.getFileDir());
            String saveFileName = fileSave(rootLocation.toString(), file);
            FileDto fileDto = new FileDto();
            fileDto.setFileName(file.getOriginalFilename());
            fileDto.setSaveFileName(saveFileName);
            fileDto.setContentType(file.getContentType());
            fileDto.setSize(file.getResource().contentLength());
            fileDto.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + '/' + saveFileName);
            return fileRepository.save(fileDto.toEntity());

        } catch (IOException e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public FileDto findOne(Long id) throws FileNotFoundException {
        Optional<FileEntity> fileEntityWrapper = fileRepository.findById(id);
        if (fileEntityWrapper.isEmpty()) {
            throw new FileNotFoundException();
        }
        return this.convertEntityToDto(fileEntityWrapper.get());
    }

    /**
     * random uuid를 생성하여 원본 파일명과 혼합하여 리턴
     *
     * @param rootLocation rootLocation
     * @param file         file
     * @return saveFileName
     * @throws IOException IOException
     */
    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            log.info("make dir : {}", mkdirs);
        }

        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }

    /**
     * 이미지 가져오기
     *
     * @param id id
     * @return FileDto
     * @throws Exception Exception
     */
    public FileDto findById(Long id) throws Exception {
        Optional<FileEntity> fileEntityWrapper = fileRepository.findById(id);
        if (fileEntityWrapper.isEmpty()) {
            throw new Exception("Not found file.");
        }
        return this.convertEntityToDto(fileEntityWrapper.get());
    }

    /**
     * 게시글 등록/수정시 file 테이블에 board_id를 업데이트 한다.
     *
     * @param boardId  boardId
     * @param contents contents
     */
    public void updateBoardIdByIdIn(Long boardId, String contents) {
        List<Long> fileIdList = this.getImageIdFromSrc(contents);
        if (fileIdList.size() > 0) {
            fileRepository.updateBoardIdByIdIn(boardId, fileIdList);
        }
    }

    /**
     * 게시글이 삭제되면 이미지 파일과 File db 정보도 삭제한다.
     *
     * @param boardId boardId
     */
    public void deleteByBoardId(Long boardId) {
        List<FileEntity> fileEntities = fileRepository.findByBoardId(boardId);
        for (FileEntity fileEntity : fileEntities) {
            this.removeImages(fileEntity.getFilePath());
        }
        fileRepository.deleteByBoardId(boardId);
    }

    /**
     * 코멘트 삭제시 해당 코멘트에 업로드된 파일 삭제
     *
     * @param commentId commentId
     */
    public void deleteByCommentId(Long commentId) {
        List<FileEntity> fileEntities = fileRepository.findByCommentId(commentId);
        for (FileEntity fileEntity : fileEntities) {
            this.removeImages(fileEntity.getFilePath());
        }
        fileRepository.deleteByCommentId(commentId);
    }

    /**
     * 코멘트에 업로드한 file에 comment_id 업데이트
     *
     * @param commentId commentId
     * @param contents  contents
     */
    public void updateCommentIdByIdIn(Long commentId, String contents) {
        List<Long> fileIdList = this.getImageIdFromSrc(contents);
        if (fileIdList.size() > 0) {
            fileRepository.updateCommentIdByIdIn(commentId, fileIdList);
        }
    }

    /**
     * 이미지 단일 삭제
     *
     * @param id file id
     * @throws FileNotFoundException FileNotFoundException
     */
    public void deleteById(Long id) throws FileNotFoundException {
        FileDto fileDto = this.findOne(id);
        this.removeImages(fileDto.getFilePath());
        fileRepository.deleteById(id);
    }

    /**
     * 게시글 html src에서 이미지 id 추출
     *
     * @param content content
     * @return file id list
     */
    public List<Long> getImageIdFromSrc(String content) {
        Pattern p = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        List<Long> result = new ArrayList<>();
        Matcher matcher = p.matcher(content);
        while (matcher.find()) {
            result.add(Long.valueOf(matcher.group(1).split("/")[4]));
        }
        return result;
    }

    /**
     * 이미지 삭제
     *
     * @param path 이미지 경로
     */
    public void removeImages(String path) {
        try {
            File targetFile = new File(path);
            if (targetFile.delete()) {
                log.info("delete image : " + path);
            } else {
                log.info("not delete image : " + path);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
